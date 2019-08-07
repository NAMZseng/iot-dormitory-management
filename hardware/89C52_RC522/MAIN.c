#include<reg52.h>
#include<main.h>
#include<mfrc522.h>

unsigned char UID[5],Temp[4];

void feeb(){ //寻卡成功响应函数
	ff=0;
	delay_10ms(1);
	ff=1;
	delay_10ms(1);
}

void Auto_Reader(void){
  while(1){
    if(PcdRequest(0x52,Temp)==MI_OK){  //寻卡，成功后Temp数组为卡类型
      if(PcdAnticoll(UID)==MI_OK){	  //防冲突，UID数组数据为卡序列号
           CALL_isr_UART();			  //开串口中断将UID数组前四个字节上传到串口调试助手
		   feeb();                    //调用蜂鸣器提示           
      }
    }else ff = 0;//寻卡失败
  } 
}

void InitializeSystem(){
	SCON=0X50;			//设置为工作方式1
	TMOD=0X21;			//设置计数器工作方式2
	PCON=0X80;			//波特率加倍
	TH1=0XF3;		    //计数器初始值设置，注意波特率是4800的
	TL1=0XF3;
	REN = 1;
	ES=1;						//打开接收中断
	EA=1;						//打开总中断
	TR1=1;		
	ff = 0;
    PcdReset();
    PcdAntennaOff(); 
    PcdAntennaOn();  
	M500PcdConfigISOType( 'A' );
}

void isr_UART(void) interrupt 4 using 1{
    unsigned char i;
	if(TI){
		TI=0;
		for(i=0;i<4;i++){
			SBUF=UID[i];
			while(!TI);
			TI=0;			
		}
		REN=1;
	}
}

void main( ){   
    InitializeSystem( );
	Auto_Reader();
}