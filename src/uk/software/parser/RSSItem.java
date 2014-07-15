package uk.software.parser;

import java.io.Serializable;

public class RSSItem implements Serializable{
	private static final long serialVersionUID = 1L;
	private String _title = null;
	private String _description = null;
	private String _date = null;
	private String _image = null;
	private String _link = null;
	private String _video = null;
	
	void setTitle(String title) {
		_title = title;
	}

	void setDescription(String description) {
		_description = description;
	}

	void setDate(String pubdate) {
		_date = pubdate;
	}

	void setImage(String image) {
		_image = image;
	}
	void setVideo(String video){
		_video = video;
	}

	void setLink(String link){
		_link = link;
	}
	
	public String getTitle() {
		return _title;
	}

	public String getDescription() {
		return _description;
	}

	public String getDate() {
		return _date;
	}

	public String getImage() {
		return _image;
	}
	public String getVideo(){
		return _video;
	}
	public String getLink(){
		return _link;
	}

}
