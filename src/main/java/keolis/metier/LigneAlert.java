package keolis.metier;

public class LigneAlert {
	
	private String title;
	private String startTime;
	private String endTime;
	private String [] lignes;
	private String detail;
	private String majordisturbance;
	private String travaux;
	
	public String getTravaux() {
		return travaux;
	}
	public void setTravaux(String travaux) {
		this.travaux = travaux;
	}
	public String getMajordisturbance() {
		return majordisturbance;
	}
	public void setMajordisturbance(String majordisturbance) {
		this.majordisturbance = majordisturbance;
	}
	public LigneAlert(String title, String startTime, String endTime,
			String[] lignes, String detail) {
		this.title = title;
		this.startTime = startTime;
		this.endTime = endTime;
		this.lignes = lignes;
		this.detail = detail;
	}
	public LigneAlert() {
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String[] getLignes() {
		return lignes;
	}
	public void setLignes(String[] lignes) {
		this.lignes = lignes;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	

}
