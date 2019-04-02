package com.harmazing.Thread;

import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.protobuf.ByteString;
import com.harmazing.Config;
import com.harmazing.protobuf.CommandProtos;
import com.harmazing.protobuf.MessageProtos;
import com.harmazing.server.DefaultServerHandler;
import com.harmazing.service.DeviceService;
import com.harmazing.service.impl.DeviceServiceImpl;
import com.harmazing.util.AirconUtil;
import com.harmazing.util.MessageUtil;
import com.harmazing.util.ProtobufUtil;

import io.netty.channel.ChannelHandlerContext;

public class IrPacketHandlerThread implements Runnable {
	public final static Logger LOGGER = LoggerFactory.getLogger(DefaultServerHandler.class);
	
	ChannelHandlerContext ctx = null; 
    CommandProtos.IrPacket ir_packet = null; 
    int seq_num = 0; 
    String session = null; 
    String dev_mac = ""; 
    
	
	public IrPacketHandlerThread(ChannelHandlerContext ctx, 
			                     CommandProtos.IrPacket ir_packet, 
			                     int seq_num, 
			                     String session, 
			                     String dev_mac){
		this.ctx = ctx;
		this.ir_packet = ir_packet;
		this.seq_num   = seq_num;
		this.session   = session;
		this.dev_mac   = dev_mac;	
	}
	
	//////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void run() {
		LOGGER.debug("Start IrPacketHandlerThread.run, dev_mac=" + dev_mac);
		
		DeviceService devSrv = new DeviceServiceImpl();
		handleIrCode(ctx, ir_packet, seq_num, session, dev_mac, devSrv);
		
		LOGGER.debug("End IrPacketHandlerThread.run, dev_mac=" + dev_mac);
	}
	
	////////////////////////////////////////////////////////////////////////////////////
	private void handleIrCode(ChannelHandlerContext ctx, CommandProtos.IrPacket ir_packet, int seq_num, String session, String dev_mac, DeviceService dev_srv){
		int rcu_id  = -1;
		ByteString main = ir_packet.getMain();
			
		String main_str = ProtobufUtil.convertByteString2String(main).trim();										
		String modsig="0160010";

		if( ir_packet.hasRcuId()){								
			rcu_id  = ir_packet.getRcuId();
			modsig = MessageUtil.getModsig(rcu_id, "", main_str);
			if(modsig.isEmpty()){
				LOGGER.debug("IrPacketHandlerThread.handleIrCode modsig is invalid(empty), do nothing");
				return;
			}				
			
			AirconUtil.updateAcByModsig(dev_mac, modsig);
						
		}else{
			List<Integer> length_list = ir_packet.getLengthList();
			String str_length = "";
			for( int i=0; i<length_list.size(); ++i){
				str_length += length_list.get(i);
				str_length += " ";
			} 
			
			JSONObject json_obj = MessageUtil.getRcuInfo(rcu_id, str_length.trim(), main_str);
			if( json_obj==null ||  (int)json_obj.get("status") != 0){
				LOGGER.error("IrPacketHandlerThread.handleIrCode: pairing failed, do nothing!");
				return;
			}
			
			modsig = (String)json_obj.get("modsig");
			if(!isOnOffBitValid(modsig)){
				LOGGER.error("IrPacketHandlerThread.handleIrCode: pairing failed with invlaid modsig("+ modsig + "), do nothing.");
				return;
			}
			
			rcu_id            = (int)json_obj.get("rcu_id");			
			int frequency     = (int)json_obj.get("frequency");
			String duty_cycle = (String)json_obj.get("duty_cycle");
			JSONArray lpulse  = json_obj.getJSONArray("pulse");
			JSONArray llength = json_obj.getJSONArray("length");	
						
			try{
		        String[] dc_array= duty_cycle.split("/");	
		        int duty_cycle_numerator = Integer.valueOf(dc_array[0]);
				int duty_cycle_denominator  = Integer.valueOf(dc_array[1]);
				
				CommandProtos.RcuUpdateCommand.Builder rct_builder = MessageUtil.createRcuUpdateCmdBuilder( 
						rcu_id, 
						frequency,
						duty_cycle_numerator,
						duty_cycle_denominator,
						lpulse,
						llength);

				CommandProtos.IrControl.Builder irctl_builder = MessageUtil.createIrControlMessage( 
												dev_mac, 
												CommandProtos.IrControl.CommandType.IR_RCU_UPDATE,
												rct_builder, 
												null);
				CommandProtos.Command.Builder cmd_builder = CommandProtos.Command.newBuilder();
				cmd_builder.setIrControl( irctl_builder);
				
				MessageProtos.SpmMessage.Builder spm_builder = MessageProtos.SpmMessage.newBuilder();
				MessageProtos.SpmMessage.Header.Builder head_builder = MessageProtos.SpmMessage.Header.newBuilder();
				head_builder.setType( MessageProtos.SpmMessage.MsgType.COMMAND);
				head_builder.setSeqnum(seq_num);
				head_builder.setSession( session);
				
				spm_builder.setHeader( head_builder);						
				spm_builder.setControlCommand( cmd_builder);						
				ctx.writeAndFlush(spm_builder.build());	
				
			}catch(Exception e){
				e.printStackTrace();
			}
			
			AirconUtil.updateRcuId(dev_mac, rcu_id);
			AirconUtil.updateAcByModsig(dev_mac, modsig );
			
			LOGGER.debug("DefaultServerHandler.handleIrCode succesfully.");
		}				
	}
	
	
	
	private boolean isOnOffBitValid(String modsig){
		try{
			Integer.valueOf(modsig.substring(6,7));
			return true;
		}catch(Exception e){
			LOGGER.error("*** OnOff status is invalid, do nothing.");
			e.printStackTrace();
		}
		
		return false;
	}
}
