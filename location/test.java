function onRequest(request, response, modules) {

    // //1.�����ϴ�����
    // var name = request.body.name;
    // if(name == 'bmob') {
    //     response.end('��ӭʹ��Bmob');
    // }
    // else {
    //     response.end('�����������������');
    // }
    
    // //2.�������ݿ���󣬲�ѯ��������
    // var db = modules.oData;
    // //��ȡAreaLocationInfo���е�����ֵ
    // db.find({
    //     "table":"LocationInfo",
    //     "order":"id",        //���ֶ����У���д�������Ĭ����������ǰ����ӡ�-���ɱ�Ϊ���� 
    //                             //�����д��Ĭ�ϰ��մ���ʱ����ȡ   
    //     // "limit":10,       //һҳ���ض�������¼��Ĭ����0��Ҳ���ǻ���ʾ����
    //     "where":{"id":5}     //��ѯ��������������Ӷ��
    //     // "skip":2          //ÿҳ��ʾ�ļ�¼��offset����������skip����¼������ʾ
    // },function(err,data){
    //     //�����ڵ������������ַ�����ʽ���ͳ�ȥ
    //     // response.end(data);
        
    //     //��String����ת��ΪJSON����
    //     var dataObject = JSON.parse(data);
    //     //�������JSON����ע���ʱ�õ��Ļ��������ݿ�������ݵĶ��󣬻�Ҫ��һ���ӹ���ԭ����ʱûŪ��
    //     for(var results in dataObject)
    //     {
    //         //��ȡ���е�JSON���󲢷�����һ������resultArr����
    //         var resultArr = dataObject[results];
    //         var str = "";
    //         //�����õ��Ľ��
    //         for(var oneline in resultArr)
    //         {
    //             //resultArr[oneline]��ʾ���ǵ���JSON���󣬺���ɸ���Ա��������
    //             str = str + "      " + oneline + " : " + resultArr[oneline].longitude + "\n";
    //         }
    //         response.send(str);
    //     }
        
        
        
    // }
    // );
    
    // //3.��ȡ��ļ�¼��
    // var db = modules.oData;
    // //��ȡ��LocationInfo���ܼ�¼��
    // db.find({
    //   "table":"LocationInfo",
    //   "limit":0,
    //   "count":1
    // },function(err,data){
    //     resultObject = JSON.parse(data);
    //     count = resultObject.count;
    //     response.end("���¼����" + count);
    // });
    
    // //4.�޸�����
    // var db = modules.oData;
    // db.update({
    //   "table":"LocationInfo",
    //   "objectId":"8cdb312248",
    //   "data":{"speed":1000}
    // },function(err,data){
    //     response.end("success");
    // });
    
    // //5.�������
    // var db = modules.oData;
    // db.insert({
    //   "table":"LocationInfo",
    //   "data":{"id":2,"speed":12,"time":"20130102"}
    // },function(err,data){
        
    // });
    
    // //�������1
    // var arr = new Array(4);
    // for(var i = 0;i<arr.length;i++)
    // {
    //     arr[i] = i;
    // }
    // var str = "";
    // for(var one in arr)
    // {
    //     str = str + one + ":" + arr[one] + "\n";
    // }
    // response.end(str);
    
    // //�������2 + ��ѯ�������� + ��ѧ����
    // var arr = new Array(4);
    // var db = modules.oData;
    // db.findOne({
    //   "table":"LocationInfo",
    //   "objectId":"14fc20fd60"
    // },function(err,data)
    // {
    //     var dataObject = JSON.parse(data);
        
    //     // arr[0] = dataObject.id;
    //     // arr[1] = dataObject.longitude;
    //     // arr[2] = dataObject.latitude;
    //     // arr[3] = dataObject.time;
        
    //     arr[0] = Math.round(5.3);
    //     arr[1] = Math.max(12,3,"a".charCodeAt());
    //     arr[2] = Math.sqrt(9);
    //     arr[3] = 100/2;
        
    //     var str = "";
    //     for(var one in arr)
    //     {
    //         str = str + one + ":" + arr[one] + "\n";
    //     }
    //     response.end(str);
    // });
    
    // //�ַ����������򣬿ɽ�ϲ�ѯ���ݿ��where������ȡĳЩ��Ӧ�����ݼ�¼
    // var arr = ['faa','bab','aaa','bac','0a-14','1f-32'];
    // var arrSort = arr.sort();
    // var str = "";
    // for(var i in arrSort)
    // {
    //     str += arrSort[i] + " ";
    // }
    // response.end(str);
    
    // //��һ�ֽ��������鷢�͵ķ���
    // response.end(arrSort.toString());
    
    //����LocationӦ������
    var db = modules.oData;
    var arr = new Array(5);
    var name = request.body.name;
    if(name == 'getData')
    {
        db.findOne({
            "table":"LocationInfo",
            "objectId":"14fc20fd60"
        },function(err,data){
            response.end(data);
        });
        
    }
    //��ȡ�豸�ϴ�������
    else if(name == 'sendData')
    {
        var str = request.body.Sample;
        var dataObject = JSON.parse(str);
        
        arr[0] = dataObject.id;
        arr[1] = dataObject.longitude;
        arr[2] = dataObject.latitude;
        arr[3] = dataObject.time;
        arr[4] = dataObject.speed;
        
        var str2 = "";
        for(var i = 0;i<arr.length;i++)
        {
            str2 += arr[i] + "\n";
        }
        
        response.end(str2);
    }
    //�������ݿ��ȡ��ص����ݲ�����
    else if(name == 'getLocation')
    {
        var str = request.body.Sample;
        var uploadData = JSON.parse(str);
        //���������洢�ϴ����ݵ�����
        var arrUpload = new Array(5);
        arrUpload[0] = uploadData.id;
        arrUpload[1] = uploadData.longitude;
        arrUpload[2] = uploadData.latitude;
        arrUpload[3] = uploadData.time;
        arrUpload[4] = uploadData.speed;
        
        
        db.find({
            "table":"LocationInfo",
            "where":{"speed":arrUpload[4]}
        },function(err,data)
        {
            //����������ת����JSON���ݸ�ʽ
            var allData = JSON.parse(data);
            //����JSON����
            for(var results in allData)
            {
                //�����е����ݷ���һ��JSON����
                var resultArr = allData[results];
                var strResult = "";
                //��������õ���Ӧ��JSON����
                for(var oneline = 0; oneline < resultArr.length; oneline++)
                {
                    strResult += oneline + ":" + resultArr[oneline].longitude + "\n";
                }
                response.send(strResult);
            }
            
        });
        
        
    }
    else if(name == 'getAllData')
    {
        db.find({
            "table":"AreaLocationInfo"
        },function(err,data)
        {
            
            response.end(data);
        });
        
    }
    else
    {
        response.end('�����������������');
    }
    
    
}                                                                                                                                                                                                 