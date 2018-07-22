package net.nearbyservices.shared;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "TBL_SERVICE")
public class ServiceDTO implements Serializable {

	private static final long serialVersionUID = -687874117917352477L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "ID")
	private Long id;

	@Column(name = "TITLE", nullable = false)
	private String title;

	@Column(name = "DETAIL", columnDefinition="TEXT")
	private String detail;

	@Column(name = "AUTOR", nullable = false)
	private String autor;
	
	

	public ServiceDTO() {
		
	}

	public ServiceDTO(Long id, String title, String detail, String autor) {
		this.id = id;
		this.title = title;
		this.detail = detail;
		this.autor = autor;
	}

	public ServiceDTO(ServiceDTO serviceDTO) {
		this.id = serviceDTO.getId();
		this.title = serviceDTO.getTitle();
		this.detail = serviceDTO.getDetail();
		this.autor = serviceDTO.getAutor();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((autor == null) ? 0 : autor.hashCode());
		result = prime * result + ((detail == null) ? 0 : detail.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ServiceDTO other = (ServiceDTO) obj;
		if (autor == null) {
			if (other.autor != null)
				return false;
		} else if (!autor.equals(other.autor))
			return false;
		if (detail == null) {
			if (other.detail != null)
				return false;
		} else if (!detail.equals(other.detail))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ServiceDTO [id=" + id + ", title=" + title + ", detail=" + detail + ", autor=" + autor + "]";
	}

}
