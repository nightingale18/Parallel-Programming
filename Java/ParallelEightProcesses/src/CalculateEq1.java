import mpi.*;
import java.util.Arrays;

public class CalculateEq1 {

 public static void main(String[] args) throws Exception {
  MPI.Init(args);
  int rank = MPI.COMM_WORLD.getRank();

  int P = 8;
  int N = 16;
  int H = N / P;
  int[] MXR_H = new int[N * N];
  int[] MXRD_H = new int[N];
  int[] CminZ_H = new int[H];
  int sum1;


  //input vectors and matrixes
  int cur_MR_Z_H = 0, cur_A = 0, cur_C = 0;
  long m = System.currentTimeMillis();
  switch (rank) {
   case 0:
    {
     cur_C = 2 * H;

     cur_MR_Z_H = N;
     cur_A = 2 * H;
     break;
    }
   case 1:
    {
     cur_C = 4 * H;

     cur_MR_Z_H = H;
     cur_A = 4 * H;
     break;
    }
   case 2:
    {
     cur_C = 3 * H;

     cur_MR_Z_H = H;
     cur_A = 3 * H;
     break;
    }
   case 3:
    {
     cur_C = 2 * H;

     cur_MR_Z_H = 2 * H;
     cur_A = 2 * H;
     break;
    }
   case 4:
    {
     cur_C = H;

     cur_MR_Z_H = 2 * H;
     cur_A = H;
     break;
    }
   case 5:
    {
     cur_C = N;

     cur_MR_Z_H = H;
     cur_A = N;
     break;
    }
   case 6:
    {
     cur_C = 3 * H;

     cur_MR_Z_H = 3 * H;
     cur_A = 3 * H;
     break;
    }
   case 7:
    {
     cur_C = H;

     cur_MR_Z_H = 2 * H;
     cur_A = H;
     break;
    }
  }

  int[] MR_H = new int[cur_MR_Z_H * N];
  int[] MX = new int[N * N];
  int[] D = new int[N];
  int[] Cmin = new int[H];
  int[] C = new int[cur_C];
  int[] A = new int[N];
  int[] A_H = new int[cur_A];
  int[] C_H = new int[H];
  int[] Z_H = new int[cur_MR_Z_H];

  if (rank == 0) {
   for (int i = 0; i < N; i++) {
    Z_H[i] = i;
    for (int j = 0; j < N; j++) {
     MR_H[i * N + j] = i + j;
    }
   }

  } else if (rank == 1) {
   for (int i = 0; i < N; i++) {
    D[i] = i;
    for (int j = 0; j < N; j++) {
     MX[i * N + j] = i + j;
    }
   }

  } else if (rank == 5) {
   for (int i = 0; i < C.length; i++) {
    C[i] = i + 1;
   }
  }
  System.out.println("start " + rank);
  MPI.COMM_WORLD.Barrier();

  if (rank == 0) {

   MPI.COMM_WORLD.Recv(D, 0, D.length, MPI.INT, 3, 0);
   MPI.COMM_WORLD.Recv(MX, 0, MX.length, MPI.INT, 3, 0);

   MPI.COMM_WORLD.Send(D, 0, N, MPI.INT, 6, 0);
   MPI.COMM_WORLD.Send(MX, 0, N * N, MPI.INT, 6, 0);
  } else if (rank == 1) {
   MPI.COMM_WORLD.Send(D, 0, N, MPI.INT, 2, 0);

   MPI.COMM_WORLD.Send(MX, 0, N * N, MPI.INT, 2, 0);


   MPI.COMM_WORLD.Send(D, 0, N, MPI.INT, 5, 0);
   MPI.COMM_WORLD.Send(MX, 0, N * N, MPI.INT, 5, 0);


  } else if (rank == 2) {

   MPI.COMM_WORLD.Recv(D, 0, D.length, MPI.INT, 1, 0);
   MPI.COMM_WORLD.Recv(MX, 0, MX.length, MPI.INT, 1, 0);

   MPI.COMM_WORLD.Send(D, 0, N, MPI.INT, 3, 0);
   MPI.COMM_WORLD.Send(MX, 0, N * N, MPI.INT, 3, 0);

   MPI.COMM_WORLD.Send(D, 0, N, MPI.INT, 4, 0);
   MPI.COMM_WORLD.Send(MX, 0, N * N, MPI.INT, 4, 0);
  } else if (rank == 3) {


   MPI.COMM_WORLD.Recv(D, 0, D.length, MPI.INT, 2, 0);
   MPI.COMM_WORLD.Recv(MX, 0, MX.length, MPI.INT, 2, 0);

   MPI.COMM_WORLD.Send(D, 0, N, MPI.INT, 0, 0);
   MPI.COMM_WORLD.Send(MX, 0, N * N, MPI.INT, 0, 0);
  } else if (rank == 4) {

   MPI.COMM_WORLD.Recv(D, 0, D.length, MPI.INT, 2, 0);
   MPI.COMM_WORLD.Recv(MX, 0, MX.length, MPI.INT, 2, 0);


   MPI.COMM_WORLD.Send(D, 0, N, MPI.INT, 7, 0);
   MPI.COMM_WORLD.Send(MX, 0, N * N, MPI.INT, 7, 0);
  } else if (rank == 5) {

   MPI.COMM_WORLD.Recv(D, 0, D.length, MPI.INT, 1, 0);
   MPI.COMM_WORLD.Recv(MX, 0, MX.length, MPI.INT, 1, 0);
  } else if (rank == 6) {

   MPI.COMM_WORLD.Recv(D, 0, D.length, MPI.INT, 0, 0);
   MPI.COMM_WORLD.Recv(MX, 0, MX.length, MPI.INT, 0, 0);
  } else if (rank == 7) {

   MPI.COMM_WORLD.Recv(D, 0, D.length, MPI.INT, 4, 0);
   MPI.COMM_WORLD.Recv(MX, 0, MX.length, MPI.INT, 4, 0);
  }
  MPI.COMM_WORLD.Barrier();
  if (rank == 0) {
   MPI.COMM_WORLD.Recv(C, 0, C.length, MPI.INT, 6, 0);
   MPI.COMM_WORLD.Send(C, H, H, MPI.INT, 4, 0);
  } else if (rank == 1) {

   MPI.COMM_WORLD.Recv(C, 0, C.length, MPI.INT, 5, 0);
   MPI.COMM_WORLD.Send(C, H, 3 * H, MPI.INT, 2, 0);

  } else if (rank == 2) {

   MPI.COMM_WORLD.Recv(C, 0, C.length, MPI.INT, 1, 0);
   MPI.COMM_WORLD.Send(C, H, 2 * H, MPI.INT, 3, 0);

  } else if (rank == 3) {

   MPI.COMM_WORLD.Recv(C, 0, C.length, MPI.INT, 2, 0);
   MPI.COMM_WORLD.Send(C, H, H, MPI.INT, 7, 0);

  } else if (rank == 4) {

   MPI.COMM_WORLD.Recv(C, 0, C.length, MPI.INT, 0, 0);

  } else if (rank == 5) {

   MPI.COMM_WORLD.Send(C, H, 3 * H, MPI.INT, 6, 0);
   MPI.COMM_WORLD.Send(C, 4 * H, 4 * H, MPI.INT, 1, 0);
  } else if (rank == 6) {
   MPI.COMM_WORLD.Recv(C, 0, C.length, MPI.INT, 5, 0);
   MPI.COMM_WORLD.Send(C, H, 2 * H, MPI.INT, 0, 0);
  } else if (rank == 7) {

   MPI.COMM_WORLD.Recv(C, 0, C.length, MPI.INT, 3, 0);
  }

  //MR Z
  if (rank == 0) {
   //Alternative way
   //MPI.COMM_WORLD.Isend(MR_H, H * N, 2 * H * N, MPI.INT, 3, 0)
   MPI.COMM_WORLD.Send(MR_H, H * N, 2 * H * N, MPI.INT, 3, 0);
   MPI.COMM_WORLD.Send(Z_H, H, 2 * H, MPI.INT, 3, 0);
   MPI.COMM_WORLD.Send(MR_H, 3 * H * N, 2 * H * N, MPI.INT, 4, 0);
   MPI.COMM_WORLD.Send(Z_H, 3 * H, 2 * H, MPI.INT, 4, 0);
   MPI.COMM_WORLD.Send(MR_H, 5 * H * N, 3 * H * N, MPI.INT, 6, 0);
   MPI.COMM_WORLD.Send(Z_H, 5 * H, 3 * H, MPI.INT, 6, 0);

  } else if (rank == 1) {
   MPI.COMM_WORLD.Recv(MR_H, 0, MR_H.length, MPI.INT, 7, 0);
   MPI.COMM_WORLD.Recv(Z_H, 0, Z_H.length, MPI.INT, 7, 0);

  } else if (rank == 2) {

   MPI.COMM_WORLD.Recv(MR_H, 0, MR_H.length, MPI.INT, 3, 0);
   MPI.COMM_WORLD.Recv(Z_H, 0, Z_H.length, MPI.INT, 3, 0);

  } else if (rank == 3) {

   MPI.COMM_WORLD.Recv(MR_H, 0, MR_H.length, MPI.INT, 0, 0);
   MPI.COMM_WORLD.Recv(Z_H, 0, Z_H.length, MPI.INT, 0, 0);

   MPI.COMM_WORLD.Send(MR_H, H * N, H * N, MPI.INT, 2, 0);
   MPI.COMM_WORLD.Send(Z_H, H, H, MPI.INT, 2, 0);

  } else if (rank == 4) {

   MPI.COMM_WORLD.Recv(MR_H, 0, MR_H.length, MPI.INT, 0, 0);
   MPI.COMM_WORLD.Recv(Z_H, 0, Z_H.length, MPI.INT, 0, 0);

   MPI.COMM_WORLD.Send(MR_H, H * N, H * N, MPI.INT, 5, 0);
   MPI.COMM_WORLD.Send(Z_H, H, H, MPI.INT, 5, 0);

  } else if (rank == 5) {

   MPI.COMM_WORLD.Recv(MR_H, 0, MR_H.length, MPI.INT, 4, 0);
   MPI.COMM_WORLD.Recv(Z_H, 0, Z_H.length, MPI.INT, 4, 0);

  } else if (rank == 6) {

   // Alternative way
   //MPI.COMM_WORLD.Irecv(MR_H, 0, MR_H.length, MPI.INT, 0, 0)
   MPI.COMM_WORLD.Recv(MR_H, 0, MR_H.length, MPI.INT, 0, 0);
   MPI.COMM_WORLD.Recv(Z_H, 0, Z_H.length, MPI.INT, 0, 0);

   MPI.COMM_WORLD.Send(MR_H, H * N, 2 * H * N, MPI.INT, 7, 0);
   MPI.COMM_WORLD.Send(Z_H, H, 2 * H, MPI.INT, 7, 0);

  } else if (rank == 7) {

   MPI.COMM_WORLD.Recv(MR_H, 0, MR_H.length, MPI.INT, 6, 0);
   MPI.COMM_WORLD.Recv(Z_H, 0, Z_H.length, MPI.INT, 6, 0);
   MPI.COMM_WORLD.Send(MR_H, H * N, H * N, MPI.INT, 1, 0);
   MPI.COMM_WORLD.Send(Z_H, H, H, MPI.INT, 1, 0);

  }
  //Find the certain a slice array to examine for min values.
  if (rank == 0) {
   C_H = Arrays.copyOfRange(C, 0, H);
  } else if (rank == 1) {
   C_H = Arrays.copyOfRange(C, 0, H);
  } else if (rank == 2) {
   C_H = Arrays.copyOfRange(C, 0, H);
  } else if (rank == 3) {
   C_H = Arrays.copyOfRange(C, 0, H);
  } else if (rank == 4) {
   C_H = Arrays.copyOfRange(C, 0, H);
  } else if (rank == 5) {
   C_H = Arrays.copyOfRange(C, 0, H);
  } else if (rank == 6) {
   C_H = Arrays.copyOfRange(C, 0, H);
  } else if (rank == 7) {
   C_H = Arrays.copyOfRange(C, 0, H);
  }
  //Find Min from all arrays and send it to the processor 5
  MPI.COMM_WORLD.Reduce(C_H, 0, Cmin, 0, C_H.length, MPI.INT, MPI.MIN, 5);
  //Send Cmin for all processors
  if (rank == 0) {
   MPI.COMM_WORLD.Recv(Cmin, 0, Cmin.length, MPI.INT, 3, 0);
   MPI.COMM_WORLD.Send(Cmin, 0, Cmin.length, MPI.INT, 6, 0);
  } else if (rank == 1) {
   MPI.COMM_WORLD.Recv(Cmin, 0, Cmin.length, MPI.INT, 5, 0);
   MPI.COMM_WORLD.Send(Cmin, 0, Cmin.length, MPI.INT, 2, 0);
  } else if (rank == 2) {
   MPI.COMM_WORLD.Recv(Cmin, 0, Cmin.length, MPI.INT, 1, 0);
   MPI.COMM_WORLD.Send(Cmin, 0, Cmin.length, MPI.INT, 3, 0);
  } else if (rank == 3) {
   MPI.COMM_WORLD.Recv(Cmin, 0, Cmin.length, MPI.INT, 2, 0);
   MPI.COMM_WORLD.Send(Cmin, 0, Cmin.length, MPI.INT, 0, 0);
  } else if (rank == 4) {
   MPI.COMM_WORLD.Recv(Cmin, 0, Cmin.length, MPI.INT, 5, 0);
   MPI.COMM_WORLD.Send(Cmin, 0, Cmin.length, MPI.INT, 7, 0);
  } else if (rank == 5) {
   MPI.COMM_WORLD.Send(Cmin, 0, Cmin.length, MPI.INT, 4, 0);
   MPI.COMM_WORLD.Send(Cmin, 0, Cmin.length, MPI.INT, 1, 0);
  } else if (rank == 6) {
   MPI.COMM_WORLD.Recv(Cmin, 0, Cmin.length, MPI.INT, 0, 0);
  } else if (rank == 7) {
   MPI.COMM_WORLD.Recv(Cmin, 0, Cmin.length, MPI.INT, 4, 0);
  }

  //A_H = Cmin*Z_H + DH * (MX*MR_H);


  for (int i = 0; i < N; i++) {
   for (int j = 0; j < H; j++) {
    sum1 = 0;
    for (int z = 0; z < N; z++) {
     sum1 = sum1 + MR_H[j * N + z] * MX[i * N + z];


    }


    MXR_H[i * N + j] = sum1;

   }
  }

  for (int i = 0; i < H; i++) {
   sum1 = 0;
   for (int j = 0; j < N; j++) {

    sum1 += D[j] * MXR_H[j * N + i];
   }

   MXRD_H[i] = sum1;
  }



  for (int i = 0; i < H; i++) {
   CminZ_H[i] = Z_H[i] * Cmin[0];
  }

  for (int i = 0; i < H; i++) {
   A[i] = MXRD_H[i] + CminZ_H[i];

  }

  //Send AH parts and gather them for getting the answer
  if (rank == 0) {
   MPI.COMM_WORLD.Recv(A, H, H, MPI.INT, 4, 0);
   MPI.COMM_WORLD.Send(A, 0, 2 * H, MPI.INT, 6, 0);
  } else if (rank == 1) {
   MPI.COMM_WORLD.Recv(A, H, 3 * H, MPI.INT, 2, 0);
   MPI.COMM_WORLD.Send(A, 0, 4 * H, MPI.INT, 5, 0);
  } else if (rank == 2) {
   MPI.COMM_WORLD.Recv(A, H, 2 * H, MPI.INT, 3, 0);
   MPI.COMM_WORLD.Send(A, 0, 3 * H, MPI.INT, 1, 0);
  } else if (rank == 3) {
   MPI.COMM_WORLD.Recv(A, H, H, MPI.INT, 7, 0);
   MPI.COMM_WORLD.Send(A, 0, 2 * H, MPI.INT, 2, 0);
  } else if (rank == 4) {
   MPI.COMM_WORLD.Send(A, 0, H, MPI.INT, 0, 0);
  } else if (rank == 5) {
   MPI.COMM_WORLD.Recv(A, H, 3 * H, MPI.INT, 6, 0);
   MPI.COMM_WORLD.Recv(A, 4 * H, 4 * H, MPI.INT, 1, 0);
  } else if (rank == 6) {
   MPI.COMM_WORLD.Recv(A, H, 2 * H, MPI.INT, 0, 0);
   MPI.COMM_WORLD.Send(A, 0, 3 * H, MPI.INT, 5, 0);
  } else if (rank == 7) {
   MPI.COMM_WORLD.Send(A, 0, H, MPI.INT, 3, 0);
  }

  MPI.COMM_WORLD.Barrier();
  if (rank == 5) {
   for (int i = 0; i < A.length; i++) {
    System.out.println("A" + A[i]);
   }
  }
  System.out.println("end " + rank + " ");
  MPI.Finalize();

  double[] v = new double[1];
  v[0] = (System.currentTimeMillis() - m);
  double[] answer = new double[1];
  MPI.COMM_WORLD.Reduce(v, 0, answer, 0, 1, MPI.DOUBLE, MPI.MAX, 0);
  if (rank == 0) {
   System.out.println(answer[0]);
  }
 }
}
