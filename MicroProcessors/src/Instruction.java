
public class Instruction {
	public String type;
	public String destination;
	public int effAddress=-1;
	public String source1,source2;
	public Instruction(String type,String destination,int effAddress) {
		this.type=type;
		this.destination=destination;
		this.effAddress=effAddress;
	}
	public Instruction(String type,String destination,String source1,String source2) {
		this.type=type;
		this.destination=destination;
		this.source1=source1;
		this.source2=source2;
	}
	public String toString() {
		if(this.effAddress!=-1)return this.type+" "+this.destination+" "+this.effAddress;
		else return this.type+" "+this.destination+" "+this.source1+" "+this.source2;
	}
}
