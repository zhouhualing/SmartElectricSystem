        var isPopLayer = false;

        var time = null;
		

            //打开弹出层

            function poplay(id){

                if(!isPopLayer){

                    //延迟1秒显示层

            time = setTimeout(function(){

                        document.getElementById(id).style.display = 'block';

            clplay();

                        isPopLayer = true;

                    }, 1000);

                }

            }

         

        //鼠标移走时执行

        function clplay(){

        clearTimeout(time);

        time = null;

        }

             

            //关闭弹出层

            function poclose(id){

                document.getElementById(id).style.display = 'none';

                isPopLayer = false;

            }
			


