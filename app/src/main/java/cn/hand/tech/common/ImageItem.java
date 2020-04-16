package cn.hand.tech.common;

import java.io.Serializable;


/**
 * 一个图片对象
 * 
 */
public class ImageItem implements Serializable {
	public enum UpStatus{SUCCESS,FAIL,ING,WAIT};
	private int id;
	private static final long serialVersionUID = 1L;
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
	public boolean isSelected = false;
	public double progress;//上传进度
	public boolean isCancel;//取消上传
	public String key;//上传的uuid
	public String image;//就是key，用来发布用
	public String imageCut;//传到七牛的文 件的的KEY的裁剪处理
	public String imageUrl;//
	public String isTopIndex;//服务器的字段，Y是封面，N否
	public UpStatus upStatus =UpStatus.WAIT;
	
	public ImageItem( String imagePath,String key, UpStatus upStatus){
		super();
		this.imagePath = imagePath;
		this.key = key;
		this.upStatus = upStatus;
	}

	public ImageItem() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImageId() {
		return imageId;
	}

	public void setImageId(String imageId) {
		this.imageId = imageId;
	}

	public String getThumbnailPath() {
		return thumbnailPath;
	}

	public void setThumbnailPath(String thumbnailPath) {
		this.thumbnailPath = thumbnailPath;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public double getProgress() {
		return progress;
	}

	public void setProgress(double progress) {
		this.progress = progress;
	}

	public boolean isCancel() {
		return isCancel;
	}

	public void setCancel(boolean isCancel) {
		this.isCancel = isCancel;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public UpStatus getUpStatus() {
		return upStatus;
	}

	public void setUpStatus(UpStatus upStatus) {
		this.upStatus = upStatus;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getImageCut() {
		return imageCut;
	}

	public void setImageCut(String imageCut) {
		this.imageCut = imageCut;
	}

	public String getIsTopIndex() {
		return isTopIndex;
	}

	public void setIsTopIndex(String isTopIndex) {
		this.isTopIndex = isTopIndex;
	}
	
}
