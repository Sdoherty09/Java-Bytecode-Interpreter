public class ConcatExample {
	
	public static void main(String[] args) {
		int[] nums2 = new int[5];
		for(int index=0;index<nums2.length;index++)
		{
			nums2[index] = index;
		}
		for(int index=0;index<nums2.length;index++)
		{
			System.out.print(nums2[index]+", ");
		}
	}
}

//Java Software Solution - Foundations of Program Design - Ninth Edition - Lewis Loftus - Page 88