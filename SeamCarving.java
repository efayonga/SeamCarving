import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.Color;

/**
 * Contient la classe main pouvant redimensionner une image d'un pourcentage horizontal et vertical spécifié dans la ligne de commmande.
 */
public class SeamCarved {

    /**
     * Affiche la BufferedImage passée en paramètre.
     * @param image image a afficher.
     */
    private static void afficherImage(BufferedImage image) {
        JFrame frame = new JFrame("Image");
        frame.getContentPane().setLayout(new FlowLayout());
        frame.getContentPane().add(new JLabel(new ImageIcon(image)));
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Affiche le tableau 2D passé en paramètre.
     * @param tab Tableau 2D d'entiers a afficher.
     */
    private static void afficher2D(int[][] tab){
        for(int x=tab.length-1; x>=0; x--){
            for(int y= 0; y<tab[0].length; y++){
                System.out.print(tab[x][y]+" ");
            }
            System.out.println();
        }
    }

    /**
     * Retourne la transposée de la Matrice passée en paramètre.
     * @param M Matrice 2D Double a transposer.
     * @return Transposée de M.
     */
    private static double[][] transpoz(double[][] M){
        double[][] Mt= new double[M[0].length][M.length];
        for(int i=0; i< M[0].length ; i++){
            for(int j=0; j<M.length; j++){
                Mt[i][j]=M[j][i];
            }
        }
        return Mt;
    }

    /**
     * Retourne l'inverse de la matrice M donnée.
     * @param M Matrice d'int.
     * @return inverse de la Matrice donnée.
     */
    private static int[][] inverse2D(int[][] M){
        int[][] Mt= new int[M.length][M[0].length];

        for(int i=0; i< M.length ; i++){
            Mt[i][0]= M[i][1];
            Mt[i][1]= M[i][0];

        }
        return Mt;
    }

    /**
     * Retourne la valeur située en M[pL][pC]. Si la valeur n'est pas dans la Matrice M, retourne l'infini.
     * @param pL ligne
     * @param pC colonne
     * @param M Matrice 2D M.
     * @return la valeur de la case si elle existe, l'infini sinon.
     */
    private static double valCase(int pL, int pC, double[][] M){
        int L= M.length, C=M[0].length;
        if (pL>L-1|| pL<0) return Double.POSITIVE_INFINITY;
        if(pC>C-1 || pC<0) return Double.POSITIVE_INFINITY;
        else return M[pL][pC];
    }

    /**
     * Retourne l'indice de la première occurence du double donné dans le tableau 1D donné.
     * @param M tableau Double.
     * @param val valeur dont l'indice est a déterminer.
     * @return indice de val.
     */
    private static int trouverIndice(double[] M, double val){
        for(int i=0; i<M.length; i++){
            if(M[i]==val){
                return i;
            }
        }
        return 0;
    }

    /**
     * Retourne le minimum de la liste de Double.
     * @param List liste de Double.
     * @return minimum de la liste Double donnée.
     */
    private static double mini(double[] List){
        double min=List[0];
        for(double i : List){
            if(i<min){
                min=i;
            }
        }
        return min;
    }

    /**
     * Retourne le minimum de la liste d'entier donnée.
     * @param List Liste d'entiers.
     * @return minimum de la liste d'entier.
     */
    private static int mini(int[] List){
        int min=List[0];
        for(int i : List){
            if(i<min){
                min=i;
            }
        }
        return min;
    }

    /**
     * Arrondi la valeur de sortie avec un nombre de décimal choisi
     * @param value Valeur en entrée non arrondie
     * @param nb Nombre de décimal
     * @return
     */
    public static double round(double value, int nb) {
        if (nb < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, nb);
        value = value * factor;
        long reduc = Math.round(value);
        return (double) reduc / factor;
    }

    /**
     * Calcule l'energie d'un pixel a partir de 2 pixels aux coordonnées supérieurs et inférieurs au pixel concerné.
     * @param pix1
     * @param pix2
     * @return energie du pixel.
     */
    private static double getPixEnergy(int pix1, int pix2){
        Color a= new Color(pix1);
        Color b= new Color(pix2);
        double pixEnergy;

        double red2= b.getRed();
        double green2= b.getGreen();
        double blue2= b.getBlue();

        double red1= a.getRed();
        double green1= a.getGreen();
        double blue1= a.getBlue();

        pixEnergy= Math.pow((red1-red2), 2) + Math.pow((green1-green2), 2) + Math.pow((blue1-blue2), 2);

        return pixEnergy;
    }

    /**
     * Calcule la matrice d'energie de la BufferedImage passée en paramètre.
     * @param image image dont l'énergie doit être calculée.
     * @return matrice d'énergie de l'image.
     */
    private static double[][] calculerMatEnergie(BufferedImage image){
        int imageLargeur= image.getWidth();
        int imageHauteur= image.getHeight();
        double[][] grilleEnergie= new double[imageLargeur][imageHauteur];


        //L'energie d'un pixel depend des pixels autour de lui
        int pixelG, pixelD, pixelH, pixelB;
        double energieHorizontale, energieVerticale;

        for(int y=0; y<imageHauteur; y++){
            for(int x=0; x<imageLargeur; x++){

                if(x==0){
                    //colonne de gauche
                    pixelG= image.getRGB(x,y);
                    pixelD= image.getRGB(x+1,y);
                }
                else if(x==imageLargeur-1){
                    //colonne de droite
                    pixelG= image.getRGB(x-1,y);
                    pixelD= image.getRGB(x,y);
                }
                else{
                    //Dans l'image
                    pixelG= image.getRGB(x-1,y);
                    pixelD= image.getRGB(x+1,y);
                }
                //Energie horizontale
                energieHorizontale= getPixEnergy(pixelG, pixelD);


                if(y==0){
                    //colonne de gauche
                    pixelH= image.getRGB(x,y);
                    pixelB= image.getRGB(x,y+1);
                }
                else if(y==imageHauteur-1){
                    //colonne de droite
                    pixelH= image.getRGB(x,y-1);
                    pixelB= image.getRGB(x,y);
                }
                else{
                    //Dans l'image
                    pixelH= image.getRGB(x,y-1);
                    pixelB= image.getRGB(x,y+1);
                }
                //Energie verticale
                energieVerticale= getPixEnergy(pixelB, pixelH);


                //insription dans la grille d'energie:
                grilleEnergie[x][y]= energieHorizontale+energieVerticale;
            }
        }
        return grilleEnergie;
    }

    /**
     *Retourne le chemin du seam de coût minimum dans le tableau donné et dans l'orientation donnée en paramètre.
     * @param M Matrice dans laquelle le Seam doit être trouvée.
     * @param orientation Orientation dans laquelle le Seam doit être trouvée.
     * @return Tableau 2D contenant le chemin du Seam de coût minimum.
     */
    private static int[][] findSeam(double[][] M, String orientation){
        int[][] chemin= new int[M.length][0];

        if(orientation.equals("Horizontal")){
            chemin = findSeam(M);
        }

        //Transposée de la matrice + Transposée des coordonnées retournées par findSeam pour pouvoir utiliser la même fonction findSeam.
        if(orientation.equals("Vertical")){
            double[][] Mt= transpoz(M);
            chemin = findSeam(Mt);
            chemin= inverse2D(chemin);
        }
        return chemin;
    }

    /**
     * @param M Matrice dans lequel trouver le Seam de coût minimal.
     * @return chemin du Seam de coût minimal.
     */
    private static int[][] findSeam(double[][] M){
        int[][] chemin= new int[M.length][2];
        double valMin= mini(M[M.length-1]);
        int indiceFin= trouverIndice(M[M.length-1], valMin);
        findSeam(M, M.length-1, indiceFin, chemin);
        return chemin;
    }

    /**
     * Fonction recursive. Calcule le chemin de coût minimum dans la Matrice M.
     */
    private static void findSeam(double[][] M, int ligne, int colonne, int[][] chemin){
        double mN,mNE, mNO;
        double valMin;
        int indiceChemin=0;

        //Cas de base (Si il n'y a plus de valeurs sous la case)
        if(valCase(ligne, colonne,M)==Double.POSITIVE_INFINITY) return;
        if(valCase(ligne-1, colonne, M)==Double.POSITIVE_INFINITY){
            chemin[ligne][0]= ligne;
            chemin[ligne][1]= colonne;
            return;
        }

        //Calcul max
        mN=  valCase(ligne-1, colonne, M);
        mNE= valCase(ligne-1, colonne-1,M);
        mNO= valCase(ligne-1, colonne+1,M);

        valMin= Math.min(mN, Math.min(mNE, mNO));

        if(valMin==mN)  indiceChemin= colonne;
        if(valMin==mNE) indiceChemin= colonne-1;
        if(valMin==mNO) indiceChemin= colonne+1;

        //Recursion
        findSeam(M, ligne-1, indiceChemin, chemin);
        chemin[ligne][0]= ligne;
        chemin[ligne][1]= indiceChemin;
    }

    /**
     * Enlève le Seam inséré en paramètre.
     * @param image Image de type BufferedImage où le seam doit être enlevé.
     * @param chemin Tableau 2D contenant les coordonnées des pixels a enlever.
     * @param direction Direction dans lequel enlever le seam. "Horizontal" ou "Verical".
     * @return L'image dépourvu du seam.
     */
    private static BufferedImage enleverSeam(BufferedImage image, int[][] chemin, String direction){
        boolean decaler=false;
        int couleur;
        int largeur= image.getWidth();
        int hauteur= image.getHeight();
        BufferedImage newImage= null;

        if(direction.equals("Horizontal")){
            newImage= new BufferedImage(largeur, hauteur-1, BufferedImage.TYPE_INT_ARGB);
        }
        if(direction.equals("Vertical")){
            newImage= new BufferedImage(largeur-1, hauteur, BufferedImage.TYPE_INT_ARGB);
        }

        //Chemin vertical a enlever
        if(direction.equals("Vertical")){
            //Parcourt ligne par ligne et on perd un pixel en tout sur la largeur
            for(int y=0; y<hauteur; y++){
                for(int x=0; x<largeur-1;x++){
                    try{
                        chemin[y][0]= chemin[y][0];
                    }
                    catch(Exception e){
                        System.out.println("y="+y+" hauteur="+image.getHeight()+ " taille chemin="+chemin.length+ " largeur="+ image.getWidth());
                        System.out.println(image.getWidth()+" "+image.getHeight());
                    }


                    if(chemin[y][0]==x && chemin[y][1]==y){
                        decaler= true;
                    }

                    if(decaler) couleur= image.getRGB(x+1,y);
                    else couleur= image.getRGB(x,y);

                    newImage.setRGB(x,y,couleur);
                }
                decaler=false;
            }
        }

        //Chemin horizontal a enlever
        else if(direction.equals("Horizontal")){
            for(int x=0; x<largeur; x++){
                for(int y=0; y<hauteur-1;y++){
                    if(chemin[x][0]==x && chemin[x][1]==y){
                        decaler= true;
                    }

                    if(decaler) couleur= image.getRGB(x,y+1);
                    else couleur= image.getRGB(x,y);

                    newImage.setRGB(x,y,couleur);
                }
                decaler=false;
            }
        }
        return newImage;
    }

    /**
     * Effectue le redimensionnement de l'image. Alternement d'enlevement de Seams horizontaux et verticaux.
     * @param image L'image a redimensionner.
     * @param pourcentH Pourcentage de redimensionnement horizontal.
     * @param pourcentV Pourcentage de redimensionnement vertical.
     * @return Retourne l'image redimensionnée.
     */
    private static BufferedImage seamCarv(BufferedImage image, int pourcentH, int pourcentV){
        System.out.println("Calculs en cours ...");

        int nbIterationHorizontal= pourcentH* image.getWidth() /100;
        int nbIterationVertical= pourcentV*   image.getHeight()/100 ;


        for(int x= 0; x<Math.max(nbIterationHorizontal, nbIterationVertical); x++){
            if(x<nbIterationHorizontal){
                double[][] grille= calculerMatEnergie(image);
                int[][] cheminV=findSeam(grille, "Vertical");
                image= enleverSeam(image, cheminV, "Vertical");
            }

            if(x<nbIterationVertical){
                double[][] grille= calculerMatEnergie(image);
                int[][] cheminH=findSeam(grille, "Horizontal");
                image= enleverSeam(image, cheminH, "Horizontal");
            }
        }

        //System.out.println("Dimension finale : "+image.getWidth()+" x "+image.getHeight());
        return image;
    }

    public static void main(String[] args){

        //start
        double lStartTime = System.nanoTime();

        BufferedImage image;
        int pourcentH, pourcentV;

        String param= args[0];
        try {
            image = ImageIO.read(new File(param));
        }
        catch (IOException e) {
            System.out.println("Erreur !\nImpossible d'ouvrir \""+param+"\"\n("+e+")");
            return;
        }

        try{
            pourcentH= Integer.parseInt(args[1]);
            pourcentV= Integer.parseInt(args[2]);
            if(pourcentH>=100 || pourcentV>=100 || pourcentH<=0 || pourcentV<=0){
                throw new Exception();
            }
        }
        catch(Exception e){
            System.out.println("Erreur !\n Veuillez entrer des valeurs de pourcentages entiers et inferieurs à 99.");
            return;
        }

        System.out.println("\nRecuperation image OK\nReduction de l'image de : "+pourcentH+"% x "+pourcentV+"%. (Horizontal x Vertical)");


        image= seamCarv(image, pourcentH, pourcentV);
        String nomFichier = param.substring(0,param.length()-4)+"_resized_"+pourcentH+'_'+pourcentV+".png";
        try{
            ImageIO.write(image, "png", new File("../Images/"+nomFichier));
        }
        catch(Exception e){
            System.out.println(e);
        }
        //afficherImage(image);
        System.out.println("\nCalculs termines. \nUn fichier redimensionne a ete cree !");

        //end
        double lEndTime = System.nanoTime()-lStartTime;
        double output= round(lEndTime/1000000000, 3);

        //time elapsed
        //long output = (lEndTime - lStartTime)/1000000000;

        System.out.println("\nTemps de calcul: "+ output + " secondes");
    }
}
