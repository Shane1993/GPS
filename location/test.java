function onRequest(request, response, modules) {

    // //1.测试上传参数
    // var name = request.body.name;
    // if(name == 'bmob') {
    //     response.end('欢迎使用Bmob');
    // }
    // else {
    //     response.end('输入错误，请重新输入');
    // }
    
    // //2.测试数据库对象，查询所有数据
    // var db = modules.oData;
    // //获取AreaLocationInfo表中的所有值
    // db.find({
    //     "table":"LocationInfo",
    //     "order":"id",        //按字段排列（可写多个），默认是升序，在前面添加”-“可变为降序 
    //                             //如果不写则默认按照创建时间提取   
    //     // "limit":10,       //一页返回多少条记录，默认是0，也就是会显示所有
    //     "where":{"id":5}     //查询条件！！！可添加多个
    //     // "skip":2          //每页显示的记录的offset，即会跳过skip个记录用于显示
    // },function(err,data){
    //     //将表内的所有数据以字符串形式发送出去
    //     // response.end(data);
        
    //     //将String类型转化为JSON数据
    //     var dataObject = JSON.parse(data);
    //     //遍历这个JSON对象，注意此时得到的还不是数据库里的数据的对象，还要进一步加工，原因暂时没弄懂
    //     for(var results in dataObject)
    //     {
    //         //获取所有的JSON对象并放置在一个数组resultArr里面
    //         var resultArr = dataObject[results];
    //         var str = "";
    //         //遍历得到的结果
    //         for(var oneline in resultArr)
    //         {
    //             //resultArr[oneline]表示的是单个JSON对象，后面可跟成员变量名称
    //             str = str + "      " + oneline + " : " + resultArr[oneline].longitude + "\n";
    //         }
    //         response.send(str);
    //     }
        
        
        
    // }
    // );
    
    // //3.获取表的记录数
    // var db = modules.oData;
    // //获取表LocationInfo的总记录数
    // db.find({
    //   "table":"LocationInfo",
    //   "limit":0,
    //   "count":1
    // },function(err,data){
    //     resultObject = JSON.parse(data);
    //     count = resultObject.count;
    //     response.end("表记录数：" + count);
    // });
    
    // //4.修改数据
    // var db = modules.oData;
    // db.update({
    //   "table":"LocationInfo",
    //   "objectId":"8cdb312248",
    //   "data":{"speed":1000}
    // },function(err,data){
    //     response.end("success");
    // });
    
    // //5.添加数据
    // var db = modules.oData;
    // db.insert({
    //   "table":"LocationInfo",
    //   "data":{"id":2,"speed":12,"time":"20130102"}
    // },function(err,data){
        
    // });
    
    // //数组操作1
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
    
    // //数组操作2 + 查询单条数据 + 数学函数
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
    
    // //字符串数组排序，可结合查询数据库的where方法获取某些对应的数据记录
    // var arr = ['faa','bab','aaa','bac','0a-14','1f-32'];
    // var arrSort = arr.sort();
    // var str = "";
    // for(var i in arrSort)
    // {
    //     str += arrSort[i] + " ";
    // }
    // response.end(str);
    
    // //另一种将整个数组发送的方法
    // response.end(arrSort.toString());
    
    //测试Location应用请求
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
    //获取设备上传的数据
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
    //遍历数据库获取相关的数据并返回
    else if(name == 'getLocation')
    {
        var str = request.body.Sample;
        var uploadData = JSON.parse(str);
        //创建用来存储上传数据的数组
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
            //将所有数据转化成JSON数据格式
            var allData = JSON.parse(data);
            //遍历JSON对象
            for(var results in allData)
            {
                //将所有的数据放入一个JSON数组
                var resultArr = allData[results];
                var strResult = "";
                //遍历数组得到相应的JSON对象
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
        response.end('输入错误，请重新输入');
    }
    
    
}                                                                                                                                                                                                 