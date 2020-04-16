package cn.hand.tech.net;
import java.io.Serializable;
import java.util.List;
 
/**
 * 分页对象
 * 
 * @author hxz
 *
 */
public class PagerG<T,M> implements Serializable{
	
//	"Message": "susscess",
//	"Status": 200,
//	"Values": {
//		"Event": [{
//				"title": "\u6d3b\u52a81",
//				"url": "",
//				"target": "bingo:\/\/ui_pay"
//			}, {
//				"title": "\u6d3b\u52a82",
//				"url": "",
//				"target": "bingo:\/\/ui_pay"
//			}
//		],
//		"Total": "5",
//		"Offset": 0,
//		"Count": 5,
//		"List": [{
//				"Proid": "3",
//				"proname": "CHANEL\/\u9999\u5948\u513f \u53ef\u53ef\u9999\u6c34 100ml",
//				"Picurl": "66884d577462350100baf306e061ec90b4e12a7da75edffa672dcb124983941512536fc49e0695f8b2b10013f3b6150a9321c4bdb1bd51d97ff8cec448624401cbd9656af7595e79d81328388dbefea2f9e29655cdfc81debd4d14df1562bbb8",
//				"Topurl": "66884d577462350100baf306e061ec90b4e12a7da75edffa672dcb1249839415b8b82c03e094ba2e8a523eb16e2c91bca3b11a3fc22d0cafca743870d6864f26073975695ab3f37e25c519289e64818b0410b872647d17c80b186dccd2956bd1",
//				"Detailsurl": "66884d577462350100baf306e061ec90b4e12a7da75edffa672dcb1249839415cb9957345ecd37fb59c6c716800ded9d77e52ba4bde3313227fe475aa0c2d40ad3226d55486c812ee6bfae75bc7c859dfe8c299504e4f4f585f5cf0f4528ce5c",
//				"Paraurl": "66884d577462350100baf306e061ec90b4e12a7da75edffa672dcb12498394154200ead3c4220bb658c8b0c7994682dfe9aaa7a3e01eb040eb9d56d705789adba64f09aed52d75379c9855d993ebc584398c721a47f9172894664bf5952f4ff5",
//				"Storyurl": "66884d577462350100baf306e061ec90b4e12a7da75edffa672dcb124983941529231ddad1997bd3039da035aab661e4b0de56841b5119c302fb98b4de89a905242b6ef69c68641232c9271b8cab40477a1e87614098a453afcd73bc03ea446c",
//				"Point": "188888",
//				"Money": "188888",
//				"Exchang": "1"
//			}, {
//				"Proid": "4",
//				"proname": "CELINE\/\u8d5b\u7433 \u5973\u58eb\u7535\u5149\u84dd\u725b\u76ae\u79cb\u5343\u5305 \u624b\u63d0\u5305 \u5355\u80a9\u5305 \u5c0f\u53f7",
//				"Prohot": "\u63a8\u8350",
//				"Picurl": "66884d577462350100baf306e061ec90b4e12a7da75edffa672dcb12498394154ba81082eb87065061b8caeb0e6577fd07852d300633a08f349195ee126b2fa252ff5ab4bbe1e5418a2eaeaf15f2b90955d377e4bf7964efe2459a7f5c21ebc0",
//				"Topurl": "66884d577462350100baf306e061ec90b4e12a7da75edffa672dcb12498394157b1d9259da4241d3b4e0e87951df02e5152f375adaefcb36e32130ac25054bd93245eae128b0c8927cf231dfa27f259656e29a9eef5b54ecd0d67173185d20db",
//				"Detailsurl": "66884d577462350100baf306e061ec90b4e12a7da75edffa672dcb124983941514a85564544b9704a410c32ba395394faaa761fd5398f8698880c3bcedac78e350d84a0c110558e01cdabad1d4e737d2f0faa96e5de9386644f7f74696f27555",
//				"Paraurl": "66884d577462350100baf306e061ec90b4e12a7da75edffa672dcb12498394150bbb60d7df001baaeeffd8d7733ddf760287a96770ea78f8c777806a50099d0c13ddfaccc94642caa67333ee2406cc977985706b3273389ecc483f26f4697301",
//				"Storyurl": "66884d577462350100baf306e061ec90b4e12a7da75edffa672dcb1249839415348a80b4a33fab4ba08f693c524398272f76786a3f52a0ba1aa6185f07b91cbbfb49499188308287fb1263644a4901801fde6ede92366a7359d589ef2eb5f1c9",
//				"Point": "18888",
//				"Money": "188888",
//				"Exchang": "1"
//			}, {
//				"Proid": "5",
//				"proname": "FENDI\/\u82ac\u8fea \u65b0\u6b3e\u9ed1\u8272\u5c0f\u725b\u76ae \u4e2d\u53f7\u5c0f\u602a\u517d\u5973\u58eb\u4e24\u7528\u624b\u63d0\u5305",
//				"Picurl": "66884d577462350100baf306e061ec90b4e12a7da75edffa672dcb124983941568ff60fbc97e97f2559ebdca791cab40c996f6c194e2454b2a9c66961dcf5120885a4033b8fc65cbd6bddcf1af2918ec929fac7336e839f9e1abcd52abd66229",
//				"Topurl": "66884d577462350100baf306e061ec90b4e12a7da75edffa672dcb124983941508713706541e1d26d9b753d67725b12ae501bf1678b500c09b898c5b33389c1ad39f0bab2c4659a8708ff9f66aa3e4ce3508a38da33f4dfb6be94695a4059e59",
//				"Detailsurl": "66884d577462350100baf306e061ec90b4e12a7da75edffa672dcb12498394152ad841dc239530312b449f0d1273a7f1ed0c1beae0b87f29fa8c5d16c669e7e2a73587f20ee593b7405a789e9facc66ec071c02630d4b14bbc443fece488838c",
//				"Paraurl": "66884d577462350100baf306e061ec90b4e12a7da75edffa672dcb1249839415adaf2881fcf84b2df5780b58ba36c510c4d7462b7f5569d71305f1994286c8c7f8fb6c5152b7d70c91b5b0f8fa05f25e14829fb935915d84571ca026eca7a541",
//				"Storyurl": "66884d577462350100baf306e061ec90b4e12a7da75edffa672dcb12498394153c6b4ef63d7d3641e12539400114ad4014de38e1cde0048bbcf57d8536773087e3fd84617306735de30d901e70a153d1703303fdbef09e5e9903956bb114aacd",
//				"Point": "69809",
//				"Money": "580000",
//				"Exchang": "1"
//			}
//}
    
	private static final long serialVersionUID = 1L;


	private String Total;
	private String Offset;
	private String Count;	
	private List<T> List;
	private List<M>  Event;
	private String  Lastmodified;
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
	public List<M> getEvent() {
		return Event;
	}
	public void setEvent(List<M> event) {
		Event = event;
	}
	public String getLastmodified() {
		return Lastmodified;
	}
	public void setLastmodified(String lastmodified) {
		Lastmodified = lastmodified;
	}

}