/**
 * 
 * @param bYear		需要计算的年  2014
 * @param bMonth 	需要计算的月  10
 * @param bDay		需要计算的日  23
 * @param pYear		需要添加的年  0
 * @param pMonth	需要添加的月  5
 * @param pDay		需要添加的日  3
 * @returns {String} 返回计算后的年月日   2015-03-26
 */
function CalculateDate(bYear,bMonth,bDay,pYear,pMonth,pDay){
	var year = parseInt(bYear);
	var month = parseInt(bMonth);
	var day = parseInt(bDay);
	var py = parseInt(pYear);
	var pm = parseInt(pMonth);
	var pd = parseInt(pDay);
	var sMonth = '';
	var sDay = '';
	if(month < 10)
		sMonth = '0'+month;
	else 
		sMonth = month;
	if(day < 10)
		sDay += '0'+day;
	else 
		sDay += day;
	var today=new Date(year+'-'+sMonth+'-'+sDay); // 获取今天时间
	today.setDate(today.getDate() + pd);
	year = today.getFullYear();
	month = today.getMonth()+1;
	day = today.getDate();
	//月份相加
	month = month + pm;
	var mYear = parseInt(month/12);
	//当前月
	month = month%12;
	year = year + mYear + py;
	var result = year +'-';
	if(month < 10)
		result += '0'+month+'-';
	else 
		result += month+'-';
	if(day < 10)
		result += '0'+day;
	else 
		result += day;
	return result;
}