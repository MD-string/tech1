package cn.hand.tech.net;
import java.io.Serializable;
import java.util.List;
 
/**
 * 分页对象
 * 
 * @author hxz
 *
 */
public class Pager<T> implements Serializable{
/*	{
	"autoCount": true,
    "first": 1,
    "hasNext": false,
    "hasPre": false,
    "last": 11,
    "nextPage": 1,
    "order": "",
    "orderBy": "",
    "orderBySetted": false,
    "pageNo": 1,
    "pageSize": 10,
    "prePage": 1,
    "result": [
        {
            "Id": -1,
            "lecturerName": "Mr Liu"
        }
    ],
    "totalCount": 1,
    "totalPages": 1
	}*/
    
	private static final long serialVersionUID = 1L;


	private String Total;
	private String Offset;
	private String Count;	
	private List<T> List;
	public String getTotal() {
		return Total;
	}
	public void setTotal(String total) {
		Total = total;
	}
	public String getOffset() {
		return Offset;
	}
	public void setOffset(String offset) {
		Offset = offset;
	}
	public String getCount() {
		return Count;
	}
	public void setCount(String count) {
		Count = count;
	}
	public List<T> getList() {
		return List;
	}
	public void setList(List<T> list) {
		List = list;
	}

}