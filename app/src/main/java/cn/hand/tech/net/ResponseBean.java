package cn.hand.tech.net;
/**
 * 响应的对象
 * 
 * @author hxz
 *
 */
public class ResponseBean<T>{
	
//	{
//	    "values": {
//	        "id": 3
//	    },
//	    "executeStatus": 0,
//	    "errorMsg": 0,
//	    "errorCode": 0
//	}
	
//	{   
//			"Status": 200,  
//	    	 "Message ": ok ,    
//	        “Values”{   }  
//	}
    
	private T Values;
	private String Status;
	private String Message;

	
	
	public T getValues() {
		return Values;
	}
	public void setValues(T values) {
		Values = values;
	}

	public String getStatus() {
		return Status;
	}

	public void setStatus(String status) {
		Status = status;
	}

	public String getMessage() {
		return Message;
	}

	public void setMessage(String message) {
		Message = message;
	}

	public boolean isSuccess() {
		return HttpUtil.isSuccess(Status);
	}
	
	

}