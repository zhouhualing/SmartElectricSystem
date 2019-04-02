	function setTab04Syn ( i )
	{
		selectTab04Syn(i);
	}
	
	function selectTab04Syn ( i )
	{
		switch(i){
			case 1:
			document.getElementById("TabTab04Con1").style.display="block";
			document.getElementById("TabTab04Con2").style.display="none";
			document.getElementById("TabTab04Con3").style.display="none";
			document.getElementById("TabTab04Con4").style.display="none";
			document.getElementById("font1").style.color="#ec7f44";
			document.getElementById("font2").style.color="#633e21";
			document.getElementById("font3").style.color="#633e21";
			document.getElementById("font4").style.color="#633e21";
			break;
			case 2:
			document.getElementById("TabTab04Con1").style.display="none";
			document.getElementById("TabTab04Con2").style.display="block";
			document.getElementById("TabTab04Con3").style.display="none";
			document.getElementById("TabTab04Con4").style.display="none";
			document.getElementById("font1").style.color="#633e21";
			document.getElementById("font2").style.color="#ec7f44";
			document.getElementById("font3").style.color="#633e21";
			document.getElementById("font4").style.color="#633e21";
			break;
			case 3:
			document.getElementById("TabTab04Con1").style.display="none";
			document.getElementById("TabTab04Con2").style.display="none";
			document.getElementById("TabTab04Con3").style.display="block";
			document.getElementById("TabTab04Con4").style.display="none";
			document.getElementById("font1").style.color="#633e21";
			document.getElementById("font2").style.color="#633e21";
			document.getElementById("font3").style.color="#ec7f44";
			document.getElementById("font4").style.color="#633e21";
			break;
			case 4:
			document.getElementById("TabTab04Con1").style.display="none";
			document.getElementById("TabTab04Con2").style.display="none";
			document.getElementById("TabTab04Con3").style.display="none";
			document.getElementById("TabTab04Con4").style.display="block";
			document.getElementById("font1").style.color="#633e21";
			document.getElementById("font2").style.color="#633e21";
			document.getElementById("font3").style.color="#633e21";
			document.getElementById("font4").style.color="#ec7f44";
			break;
		}
	}
