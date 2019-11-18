/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Back.sort;

import Engine.Config;
import Engine.HUD;
import Engine.Scene;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.gl2.GLUT;

/**
 *
 * @author ezpadaz
 */
public class MergeSort implements Scene {

    private HUD hud;
    private int[] arr;
    private float[] dp = Config.getDrawPos();
    private GLUT glut;

    public void init(HUD hud) {
        this.hud = hud;       
        //autogen del array en otra clase? :v
        arr = Config.genArray();
        glut = new GLUT();
    }

    @Override
    public void render(GL2 gl) {
        drawGraph(gl);
        hud.render(gl);
        renderText(gl,glut);
    }
    
    private void drawGraph(GL2 gl){
        float base = 0f;
        gl.glPushMatrix();
        gl.glTranslatef(dp[0],dp[1],dp[2]);
        gl.glBegin(GL2.GL_QUADS);
        for(int i = 0; i<arr.length; i++){
            float height = ((float)arr[i]/100);
            float pos = ((float)i/10);

            gl.glPushMatrix();
            float[] color = colorGrading(arr[i]);
            gl.glColor3f(color[0],color[1],color[2]);
            // base           
            gl.glVertex3f(base+pos, base, 0);
            gl.glVertex3f(base+pos, height,0);
            // altura
            gl.glVertex3f(base+pos-0.1f, height, 0);
            gl.glVertex3f(base+pos-0.1f, base, 0);
            gl.glPopMatrix();
        }
        gl.glEnd();
        gl.glPopMatrix();
        gl.glFlush();
    }

    @Override
    public float[] colorGrading(int a) {
        int n = a;
        float[] rgb = new float[3];
        if(n<=100){
            //RED FLAG
            if(n<50){
                rgb[0] = ((float)n/10);
            }else{
            rgb[0] = ((float)n/100);
            }
            rgb[1] = 0;
            rgb[2] = 0f;
            return rgb;
        }else if(n>=100 && n<=199){
            //naranja :v
            rgb[0] = 1;
            rgb[1] = ((float)n/100)-1;
            rgb[2] = 0;
            return rgb;
        }else if(n>=200 && n<=299){
            //verde
            rgb[0] = 0;
            rgb[1] = ((float)n/100)-2;
            rgb[2] = 0;
            return rgb;
            
        }else if(n>=299 && n<=399){
            // Morado
            rgb[0] = 1;
            rgb[1] = 0;
            rgb[2] = (((float)n/100)-3);
            return rgb;
        }else{
            rgb[0] = 1;
            rgb[1] = 1;
            rgb[2] = 1;
            return rgb;
        }
    }

    public void renderText(GL2 gl, GLUT glut){        
        float base = 0f;
        gl.glPushMatrix();
        gl.glTranslatef(dp[0],dp[1],dp[2]);
        for(int i= 0; i<arr.length; i++){
            float height = ((float)arr[i]/100);
            float pos = ((float)i/10);
            float[] textpos = {((base+pos)-0.08f),height+0.07f,0};         
            gl.glColor3f(1,1,1);
            gl.glRasterPos3f(textpos[0],textpos[1],textpos[2]);
            glut.glutBitmapString(GLUT.BITMAP_HELVETICA_10, Integer.toString(arr[i]));         
        }
        gl.glPopMatrix();
    }
    
    @Override
    public void startSorting() {
        Thread sort = new Thread(){
            public void run(){
                //aqui va el metodo de sorteo.
                sort(arr,0,arr.length-1);
            }
        };        
        sort.start();
    }
    
    void merge(int arr[], int l, int m, int r) 
    { 
        // Encuentra el tamaño de los 2 sub arrays.
        int n1 = m - l + 1; 
        int n2 = r - m; 
  
        /* Crea arrays  */
        int L[] = new int [n1]; 
        int R[] = new int [n2]; 
  
        /*Copia los datos a los arreglos temporales*/
        for (int i=0; i<n1; ++i) 
            L[i] = arr[l + i]; 
        for (int j=0; j<n2; ++j) 
            R[j] = arr[m + 1+ j]; 
  
  
        /* Hace merge de ls arreglos temporales. */
  
        // Index inicial del primer y segundo sub arreglo 
        int i = 0, j = 0; 
  
        // Index inicial del sub arreglo mezclado 
        int k = l; 
        while (i < n1 && j < n2) 
        { 
            //timer para el pintado en pantalla.
            try{
                Thread.sleep(Config.getDelay());
            }catch(Exception e){
                e.printStackTrace();
            }
            if (L[i] <= R[j]) 
            { 
                arr[k] = L[i]; 
                i++; 
            } 
            else
            { 
                arr[k] = R[j]; 
                j++; 
            } 
            k++; 
        } 
  
        /* Copia elementos restantes de lado izquierdo si es que quedan  */
        while (i < n1) 
        { 
            arr[k] = L[i]; 
            i++; 
            k++; 
        } 
  
        /*Copia elementos restantes de lado derecho si es que quedan. */
        while (j < n2) 
        { 
            arr[k] = R[j]; 
            j++; 
            k++; 
        } 
    } 
  
    // Funcion principal.
    void sort(int arr[], int l, int r) 
    { 
        if (l < r) 
        { 
            // Encuentra el medio. 
            int m = (l+r)/2; 
  
            // Hace sort de la primera y la segunda mitad.
            sort(arr, l, m); 
            sort(arr , m+1, r); 
  
            // Hace merge de las mitades ya con sort.
            merge(arr, l, m, r); 
        } 
    } 
}
