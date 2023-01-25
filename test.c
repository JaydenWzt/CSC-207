#include <stdio.h>




int main ()
{ 
  int a;
  printf("enter the number\n");
  scanf("%d",&a);
	
	for (int i = 2;i<a;i++)
	{
		
		if (a % i == 0){
		printf("not");
		return 0;
		}
		
	} 
	printf ("prime");

return 0;
}
