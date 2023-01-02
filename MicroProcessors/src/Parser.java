import java.util.*;
import java.io.*;
public class Parser {
	
	public Instruction instructions[];
	public Parser() throws FileNotFoundException {
		Scanner sc=new Scanner(new File("input.txt"));
		PrintWriter out=new PrintWriter(System.out);
		Instruction[]instructions=new Instruction[1000];
		String[] s=null;
		int i=0;
		while(sc.hasNext()) {
			s=sc.nextLine().split(" ");
			String a[]=s[1].split(",");
			if(a.length==2) {
				int eff=Integer.parseInt(a[1]);
				Instruction inst=new Instruction(s[0],a[0],eff);
				instructions[i++]=inst;
				
			}else {
				Instruction inst=new Instruction(s[0],a[0],a[1],a[2]);
				instructions[i++]=inst;
			}
		}
		this.instructions=instructions;
	
	
	}
	
}
