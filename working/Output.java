class Output {
 public static void main(String [] args) {
	 go();
 }
 static void go() {
	 int y = 7;
	 for(int x = 1; x < 8; x++) {
		 y++;
		 if (x > 4) {
		 	System.out.print(++y);
		 	System.out.print(" ");
		 }
		 if (y > 14) {
			 System.out.print(" x = ");
			 System.out.println(x);
			 break;
		 }
	 }
 }
} 
//Head First Java 2nd Edition Sierra & Bates Page 118 (Modified)