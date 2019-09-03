#ifndef _NODE_CONFIG_H_
#define _NODE_CONFIG_H_
#ifndef externC
# ifdef __cplusplus
#  define externC extern"C"
# else
#  define externC
# endif
#endif

enum {
    DevTemp = 1,                    //1 空气温度
    DevHumm,                        //2 空气湿度
    DevILLum,                       //3 光照度

    DevRain,                        //4 雨滴
    DevIRDist,                      //5 红外测距
    DevGas,                         //6 燃气

    DevSmoke,                       //7 烟雾
    DevFire,                        //8 火焰
    DevIRPers,                      //9 人体红外
    DevVoice,                       //10 语音识别

    DevExecuteB,                    //=33?? 开关量输出执行器

	DevHall,		//霍尔
	DevShake,		//震动

    DevExecuteA,                    // 模拟量输出执行器
    DevRemoter,                     // 红外遥控
    Dev125kReader,                  // 125kHz读卡器
    DevSpeaker,                     // 语音报警
    DevTest,                        // 功能测试
    DevBroadcastSend,               // 广播发送
    DevBroadcastReceive,            // 广播接收
    DevIRDecode,                    // 红外遥控解码

    DevRouter = 240,
	DevCoordinator,
    DevMaxNum,
};
typedef struct node_type_t {
    int id;
    const char *type;
} NODE_TYPE_STRING;

#ifdef MAIN_NODE_CONFIG
#ifdef __cplusplus
extern "C" {
#endif
static const NODE_TYPE_STRING wsncomm_nodeTypeList[] = {
    { DevTemp, "温度", },
    { DevHumm, "湿度", },
    { DevILLum, "光照", },
    { DevRain, "雨滴", },
    { DevIRDist, "距离", },
    { DevGas, "燃气", },
    { DevSmoke, "烟雾", },
    { DevFire, "火焰", },
    { DevIRPers, "安防", },
    { DevVoice, "语音", },
    { DevExecuteB, "控制B", },
    { DevHall, "霍尔", },	
    { DevShake, "震动", },
	
    { DevExecuteA, "控制A", },
    { DevRemoter, "遥控", },
    { Dev125kReader, "IC\xE5\x8D\xA1", },//"IC卡", },
    { DevIRDecode, "红外解码", },
    { DevRouter, "路由", },
    { DevCoordinator, "\xE5\x8D\x8F\xE8\xB0\x83\xE5\x99\xA8", },//"协调器", },
};
const char *wsncomm_find_nodeTypeString(int type)
{
    int left = 0;
    int right = (sizeof(wsncomm_nodeTypeList) / sizeof(wsncomm_nodeTypeList[0])) - 1;
    int middle;

    while(left <= right)
    {
        middle = (left+right) / 2;
        if(type == wsncomm_nodeTypeList[middle].id)
            return wsncomm_nodeTypeList[middle].type;
        if(type > wsncomm_nodeTypeList[middle].id)
            left = middle + 1;
        else
            right = middle - 1;
    }
    return NULL;
}

#ifdef __cplusplus
}
#endif
#else
externC const char *wsncomm_find_nodeTypeString(int type);
#endif

#ifndef _NEW_TYPES
#define _NEW_TYPES
typedef unsigned char uint8;
typedef unsigned short uint16, WORD;
typedef unsigned long uint32;
typedef uint8 byte;
#endif//_NET_TYPES

#endif //_NODE_CONFIG_H_


