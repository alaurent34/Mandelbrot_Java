package utils;
/**
 * 
 * @author gtrauchessec
 * Pair de variables
 * @param <A> Type du premier element
 * @param <B> Type du second element
 */
public class Pair<A, B> {

	protected A first;
    protected B second;
/**
 * Constructeur
 * @param a Valeur du premier element
 * @param b Valeur du second element
 */
    public Pair(A a, B b) {
        this.first = a;
        this.second = b;
    }
/**
 * @return Chaine de caratere correspondant a la pair
 */
	public String toString() {
		return "[" + first + "," + second + "]";
	}
/**
 * @return First element
 */
	public A getFirst() {
		return first;
	}
/**
 * @return Second element
 */
	public B getSecond() {
		return second;
	}
/**
 * 
 * @param first First element to set
 */
	public void setFirst(A first) {
		this.first = first;
	}
/**
 * 
 * @param second Second element to set
 */
	public void setSecond(B second) {
		this.second = second;
	}
}