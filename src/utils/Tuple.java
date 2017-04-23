package utils;
/**
 * 
 * @author gtrauchessec
 * Triplet de variables
 * @param <A> Type du premier element
 * @param <B> Type du second element
 * @param <C> Type du troisieme element
 */
public class Tuple<A, B, C> extends Pair<A, B>{

    protected C third;
    /**
     * Constructeur
     * @param a Valeur du premier element
     * @param b Valeur du second element
     * @param c Valeur du troisieme element
     */
    public Tuple(A a, B b, C c) {
    	super(a,b);
        this.third = c;
    }

/**
 * @return Chaine de caratere correspondant au triplet
 */
	public String toString() {
		return "[" + first + "," + second + "," + third + "]";
	}
/**
 * 
 * @return Third element
 */
	public C getThird() {
		return third;
	}
/**
 * 
 * @param third Third element to set
 */
	public void setThird(C third) {
		this.third = third;
	}
}