package com.bizdata.app.imagecode;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;
import me.sdevil507.base.JpaUUIDBaseEntity;

@Entity
@Table(name = "app_imagecode")
@Data
public class ImageCode extends JpaUUIDBaseEntity{
	public ImageCode(String code,Long timestamp,String sessionid){
		this.code=code;
		this.timestamp=timestamp;
		this.sessionid = sessionid;
	}
	public ImageCode(){
		
	}
	private String code;
	private Long timestamp;
	private String sessionid;
	
}
