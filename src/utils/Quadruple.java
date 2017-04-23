package utils;
/**
 * 
 * @author gtrauchessec
 * Quadruplet de variables
 * @param <A> Type du premier element
 * @param <B> Type du second element
 * @param <C> Type du troisieme element
 * @param <D> Type du quatieme element
 */
public class Quadruple<A,B,C,D> extends Tuple<A, B, C>{

	protected D fourth;
/**
 * Constructeur
 * @param a Valeur du premier element
 * @param b Valeur du second element
 * @param c Valeur du troisieme element
 * @param d Valeur du quatrieme element
 */
	public Quadruple(A a, B b, C c,D d) {
		super(a, b, c);
		this.fourth = d;
	}
/**
 * @return Fourth Element
 */
	public D getFourth() {
		return fourth;
	}
/**
 * 
 * @param fourth Fourth elemeent to set
 */
	public void setFourth(D fourth) {
		this.fourth = fourth;
	}

/**
 * @return Chaine de caratere correspondant au quadruplet
 */
	public String toString() {
		return "[" + first + "," + second + "," + third + "," + fourth + "]";
	}
}
