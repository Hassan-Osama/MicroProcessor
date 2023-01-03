import java.io.FileNotFoundException;
import java.util.Iterator;

public class Tumasulo {
	public int addLatency=1;
	public int subLatency=1;
	public int divLatency=5;
	public int MulLatency=3;
	public int loadLatency=2;
	public int saveLatency=2;
	
	public  Instruction[] instructionsA=new Instruction[30];
	public String[] memory=new String[1024];
	//public String[] regFile=new String[32];
	public mulRecord mulBuf=new mulRecord();
	public LoadBuffer loadbuf=new LoadBuffer();
	public StoreBuffers storebuf=new StoreBuffers();
	public AddBuffer addbuf=new AddBuffer();
	public RegisterFiles regFiles=new RegisterFiles();
	public Tumasulo() throws FileNotFoundException {
//		public String type;
//		public String destination;
//		public int effAddress=-1;
//		public String source1,source2;
		//this.instructions=new Parser();
		Parser inst=new Parser();
		int i=0;
		while(inst.instructions[i]!=null) {
			instructionsA[i]=new Instruction();
			instructionsA[i].type=inst.instructions[i].type;
			instructionsA[i].destination=inst.instructions[i].destination;
			instructionsA[i].effAddress=inst.instructions[i].effAddress;
			instructionsA[i].source1=inst.instructions[i].source1;
			instructionsA[i].source2=inst.instructions[i].source2;
			i++;
		}
		for(int j=0;j<memory.length;j++) {
			memory[j]="0";
		}
//		for(int n=0;n<regFile.length;n++) {
//			regFile[n]="0";
//		}
	}
	public boolean issue(Instruction i) {
		boolean issued=false;
		if(i.type.equals("l.d")) {
			if(this.loadbuf.buffer[0].busy) {
				if(this.loadbuf.buffer[1].busy) {
					if(!this.loadbuf.buffer[2].busy) {	
					int integ=Integer.parseInt(i.destination.substring(1));
					loadbuf.buffer[1].address=i.effAddress;
					loadbuf.buffer[1].busy=true;
					issued=true;
					regFiles.registers[integ].Qi="L1";
				}}
				else {
					int integ=Integer.parseInt(i.destination.substring(1));
					loadbuf.buffer[1].address=i.effAddress;
					loadbuf.buffer[1].busy=true;
					issued=true;
					regFiles.registers[integ].Qi="L1";
				}
			}else {
				int integ=Integer.parseInt(i.destination.substring(1));
				loadbuf.buffer[0].address=i.effAddress;
				loadbuf.buffer[0].busy=true;
				issued=true;
				regFiles.registers[integ].Qi="L1";
			 	
			}
		}if(i.type.toLowerCase().contains("add") || (i.type.toLowerCase().contains("sub"))) {
			if(addbuf.records[0].busy) {
				if(addbuf.records[1].busy) {
					if(!addbuf.records[2].busy) {
						addbuf.records[2].op=i.type;
						int integ=Integer.parseInt(i.source1.substring(1));
						int integ2=Integer.parseInt(i.source2.substring(1));
						int integ3=Integer.parseInt(i.destination.substring(1));
						if(regFiles.registers[integ].Qi.equals("0")) 
							addbuf.records[2].vj=regFiles.registers[integ].contents+"";
						else
							addbuf.records[2].qj=regFiles.registers[integ].Qi+"";
						if(regFiles.registers[integ2].Qi.equals("0")) 
							addbuf.records[2].vk=regFiles.registers[integ2].contents+"";
						else
							addbuf.records[2].qk=regFiles.registers[integ2].Qi+"";
						regFiles.registers[integ3].Qi="A3";
						issued=true;
						addbuf.records[2].busy=true;
						
					}
				}else {
					addbuf.records[1].op=i.type;
					int integ=Integer.parseInt(i.source1.substring(1));
					int integ2=Integer.parseInt(i.source2.substring(1));
					int integ3=Integer.parseInt(i.destination.substring(1));
					if(regFiles.registers[integ].Qi.equals("0")) 
						addbuf.records[1].vj=regFiles.registers[integ].contents+"";
					else
						addbuf.records[1].qj=regFiles.registers[integ].Qi+"";
					if(regFiles.registers[integ2].Qi.equals("0")) 
						addbuf.records[1].vk=regFiles.registers[integ2].contents+"";
					else
						addbuf.records[1].qk=regFiles.registers[integ2].Qi+"";
					regFiles.registers[integ3].Qi="A2";
					issued=true;
					addbuf.records[1].busy=true;
					
				}
			}else {
				addbuf.records[0].op=i.type;
				int integ=Integer.parseInt(i.source1.substring(1));
				int integ2=Integer.parseInt(i.source2.substring(1));
				int integ3=Integer.parseInt(i.destination.substring(1));
				if(regFiles.registers[integ].Qi.equals("0")) 
					addbuf.records[0].vj=regFiles.registers[integ].contents+"";
				else
					addbuf.records[0].qj=regFiles.registers[integ].Qi+"";
				if(regFiles.registers[integ2].Qi.equals("0")) 
					addbuf.records[0].vk=regFiles.registers[integ2].contents+"";
				else
					addbuf.records[0].qk=regFiles.registers[integ2].Qi+"";
				regFiles.registers[integ3].Qi="A1";
				issued=true;
				addbuf.records[0].busy=true;
				
			}
			}

	if(i.type.toLowerCase().contains("mul") || (i.type.toLowerCase().contains("div"))){
			if(mulBuf.record[0].busy) {
				if(!mulBuf.record[1].busy) {
					mulBuf.record[1].op=i.type;
					int integ=Integer.parseInt(i.source1.substring(1));
					int integ2=Integer.parseInt(i.source2.substring(1));
					int integ3=Integer.parseInt(i.destination.substring(1));
					if(regFiles.registers[integ].Qi.equals("0")) 
						mulBuf.record[1].Vj=regFiles.registers[integ].contents+"";
					else
						mulBuf.record[1].Qj=regFiles.registers[integ].Qi+"";
					if(regFiles.registers[integ2].Qi.equals("0")) 
						mulBuf.record[1].Vk=regFiles.registers[integ2].contents+"";
					else
						mulBuf.record[1].Qk=regFiles.registers[integ2].Qi+"";
					regFiles.registers[integ3].Qi="M2";
					issued=true;
					mulBuf.record[1].busy=true;
				}
			}else {
				mulBuf.record[0].op=i.type;
				int integ=Integer.parseInt(i.source1.substring(1));
				int integ2=Integer.parseInt(i.source2.substring(1));
				int integ3=Integer.parseInt(i.destination.substring(1));
				if(regFiles.registers[integ].Qi.equals("0")) 
					mulBuf.record[0].Vj=regFiles.registers[integ].contents+"";
				else
					mulBuf.record[0].Qj=regFiles.registers[integ].Qi+"";
				if(regFiles.registers[integ2].Qi.equals("0")) 
					mulBuf.record[0].Vk=regFiles.registers[integ2].contents+"";
				else
					mulBuf.record[0].Qk=regFiles.registers[integ2].Qi+"";
				regFiles.registers[integ3].Qi="M1";
				issued=true;
				mulBuf.record[0].busy=true;
			}
			
		}if(i.type.toLowerCase().equals("s.d") ){
		  
			if(this.storebuf.buffers[0].busy) {
				if(this.storebuf.buffers[1].busy) {
					if((!this.storebuf.buffers[2].busy)) {
					int integ=Integer.parseInt(i.destination.substring(1));
					//if(regFiles.registers[integ].Qi.equals("0")) {
					storebuf.buffers[2].address=i.effAddress;
					storebuf.buffers[2].busy=true;
					if(regFiles.registers[integ].Qi.equals("0")) 
						storebuf.buffers[2].v=regFiles.registers[integ].contents+"";
					else
						storebuf.buffers[2].q=regFiles.registers[integ].Qi+"";
					
					issued=true;
					storebuf.buffers[2].busy=true;
					
				}
					}
				else {
					int integ=Integer.parseInt(i.destination.substring(1));
					
					storebuf.buffers[1].address=i.effAddress;
					storebuf.buffers[1].busy=true;
					if(regFiles.registers[integ].Qi.equals("0")) 
						storebuf.buffers[1].v=regFiles.registers[integ].contents+"";
					else
						storebuf.buffers[1].q=regFiles.registers[integ].Qi+"";
					
					issued=true;
					storebuf.buffers[1].busy=true;
				}
			}else {
				int integ=Integer.parseInt(i.destination.substring(1));
				
				storebuf.buffers[0].address=i.effAddress;
				storebuf.buffers[0].busy=true;
				if(regFiles.registers[integ].Qi.equals("0")) 
					storebuf.buffers[0].v=regFiles.registers[integ].contents+"";
				else
					storebuf.buffers[0].q=regFiles.registers[integ].Qi+"";
				
				issued=true;
				storebuf.buffers[0].busy=true;
			 	
			}
		}
		return issued;	
	}
	
	public  void printBuffers() {
		System.out.println();
		System.out.print("Mul Buffer");
		System.out.println();
		System.out.print("M1:  "+mulBuf.record[0].op+"  busy:  "+mulBuf.record[0].busy+"  Vj:  "+mulBuf.record[0].Vj+"   Vk:  "+mulBuf.record[0].Vk+"  Qj:   "+mulBuf.record[0].Qj+"   Qk:     "+mulBuf.record[0].Qk);
		System.out.println();	
		System.out.print("M2:  "+mulBuf.record[1].op+"  busy:  "+mulBuf.record[1].busy+"  Vj:  "+mulBuf.record[1].Vj+"   Vk:  "+mulBuf.record[1].Vk+"   Qj:   "+mulBuf.record[1].Qj+"   Qk:     "+mulBuf.record[1].Qk);
		System.out.println();		
		System.out.print("---------------------------------------------");
		System.out.println();	
		System.out.print("Add Buffer");
		System.out.println();
		System.out.print("A1:  "+addbuf.records[0].op+"  busy:  "+addbuf.records[0].busy+"  Vj:  "+addbuf.records[0].vj+"  Vk:  "+addbuf.records[0].vk+"  Qj:   "+addbuf.records[0].qj+"   Qk:     "+addbuf.records[0].qk);
		System.out.println();	
		System.out.print("A2:  "+addbuf.records[1].op+"  busy:  "+addbuf.records[1].busy+"  Vj:  "+addbuf.records[1].vj+"  Vk:  "+addbuf.records[1].vk+"  Qj:   "+addbuf.records[1].qj+"   Qk:     "+addbuf.records[1].qk);
		System.out.println();	
		System.out.print("A3:  "+addbuf.records[2].op+"  busy:  "+addbuf.records[2].busy+"  Vj:  "+addbuf.records[2].vj+"  Vk:  "+addbuf.records[2].vk+"  Qj:   "+addbuf.records[2].qj+"   Qk:     "+addbuf.records[2].qk);
		
		System.out.println();		
		System.out.print("---------------------------------------------");
		System.out.println();	
		System.out.print("Load Buffer");
		System.out.println();
		System.out.print("L1:  "+loadbuf.buffer[0].address+"   busy  "+loadbuf.buffer[0].busy);
		System.out.println();	
		System.out.print("L2:  "+loadbuf.buffer[1].address+"  busy  "+loadbuf.buffer[1].busy);
		System.out.println();	
		System.out.print("L3:  "+loadbuf.buffer[2].address+"   busy  "+loadbuf.buffer[2].busy);
		
		System.out.println();		
		System.out.print("---------------------------------------------");
		System.out.println();	
		System.out.print("Store Buffer");
		System.out.println();
		System.out.print("S1:  "+storebuf.buffers[0].address+"   busy:  "+storebuf.buffers[0].busy+"   v:   "+storebuf.buffers[0].v+"  q:   "+storebuf.buffers[0].q);
		System.out.println();	
		System.out.print("S2:  "+storebuf.buffers[1].address+"   busy:  "+storebuf.buffers[1].busy+"   v:   "+storebuf.buffers[1].v+"  q:   "+storebuf.buffers[1].q);
		System.out.println();	
		System.out.print("S3:  "+storebuf.buffers[2].address+"   busy:  "+storebuf.buffers[2].busy+"   v:   "+storebuf.buffers[2].v+"  q:   "+storebuf.buffers[2].q);
		System.out.println();		
		System.out.print("---------------------------------------------");
		
		System.out.println();	
		System.out.println("Register Files");
		for(int i=0;i<32;i++)
		System.out.println("F"+i+":    Q:"+regFiles.registers[i].Qi+"   content:"+regFiles.registers[i].contents);
		
		System.out.println("-------------------------------------");
		
	}
	
	public double execute() {
		double res = 0;
		for (int i = 0; i < loadbuf.buffer.length ; i++) {
			LoadSession l=loadbuf.buffer[i];
			if (l.busy==true) {
				if (l.execTime>=loadLatency) {
					memory[l.address]="10";
//					System.out.println(memory[s.address]);
					res = Double.parseDouble(memory[l.address]);
					l.execTime=0;
				}else {
					l.execTime++;
				}
			}
		}
		for (int i = 0; i < storebuf.buffers.length; i++) {
			bufSes s= storebuf.buffers[i];
			if (s.busy==true) {
				if (s.execTime>saveLatency) {
					if (s.q.equalsIgnoreCase("")) {
						res= Double.parseDouble(s.v);
						s.execTime=0;
					}
				} else {
					if (!s.q.equalsIgnoreCase("")) {
						s.execTime++;
					}
				}
			}
		}
		for (int i = 0; i < addbuf.records.length; i++) {
			AddBufferRecord a= addbuf.records[i];
			if (a.busy==true) {
				if (a.op.toLowerCase().contains("add")) {
					if (a.execTime>addLatency) {
						if (a.qj.equalsIgnoreCase("") && a.qk.equalsIgnoreCase("")) {
							res = Double.parseDouble(a.vj) + Double.parseDouble(a.vk);
							a.execTime=0;
						}
					} else {
						if (!(a.qj.equalsIgnoreCase("") && a.qk.equalsIgnoreCase(""))) {
							a.execTime++;	
						}
					}
				} else {
					if (a.execTime>addLatency) {
						if (a.qj.equalsIgnoreCase("") && a.qk.equalsIgnoreCase("")) {
							res = Double.parseDouble(a.vj) - Double.parseDouble(a.vk);
							a.execTime=0;
						}
					} else {
						if (!(a.qj.equalsIgnoreCase("") && a.qk.equalsIgnoreCase(""))) {
							a.execTime++;	
						}
					}
				}
			}
		}
		for (int i = 0; i < mulBuf.record.length; i++) {
			mulRecordTable m =mulBuf.record[i];
			if (m.busy==true) {
				if (m.op.toLowerCase().contains("mul")) {
					if (m.execTime>MulLatency) {
						if (m.Qj.equalsIgnoreCase("") && m.Qk.equalsIgnoreCase("")) {
							res=Double.parseDouble(m.Vj)*Double.parseDouble(m.Vk);
							m.execTime=0;
						}
					}else {
						if (!(m.Qj.equalsIgnoreCase("") && m.Qk.equalsIgnoreCase(""))) {
							m.execTime++;
						}
					}
				} else {
					if (m.execTime>MulLatency) {
						if (m.Qj.equalsIgnoreCase("") && m.Qk.equalsIgnoreCase("")) {
							res=Double.parseDouble(m.Vj)/Double.parseDouble(m.Vk);
							m.execTime=0;
						}
					}else {
						if (!(m.Qj.equalsIgnoreCase("") && m.Qk.equalsIgnoreCase(""))) {
							m.execTime++;
						}
					}
				}
			}
		}
		
		return res;
	}
	
	public void run() throws FileNotFoundException {
		Tumasulo t=new Tumasulo();
		for (int i = 0; i < instructionsA.length; i++) {
			if(t.instructionsA[i]==null) {
				break;
			}
			
			System.out.println(t.instructionsA[i]);
		}
		t.issue(t.instructionsA[0]);
		for (int i = 0; i < 4; i++) {
//			t.printBuffers();
//			System.out.println("Cycle: " + i);
//			if(t.instructionsA[i]!=null) {
//				t.issue(t.instructionsA[i]);
//			}
//			t.printBuffers();
			System.out.println(t.execute());
		}
	}
	
	
public static void main(String[]args) throws FileNotFoundException {
	Tumasulo t=new Tumasulo();
	t.run();
	}
	

}
