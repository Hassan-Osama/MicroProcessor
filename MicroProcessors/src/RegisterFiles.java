
public class RegisterFiles {
public Register[] registers=new Register[32];
public RegisterFiles() {
	for(int i=0;i<registers.length;i++) {
		registers[i]=new Register();
	}
}
}
