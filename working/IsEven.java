
public class IsEven {
    public static boolean isEven(int num)
    {
		return num%2==0;
	}
	
	public static void main(String[] args)
	{	
		int num=6;
		if(isEven(num))
		{
			System.out.println("Even");
		}
		else
		{
			System.out.println("Odd");
		}
	}
}

