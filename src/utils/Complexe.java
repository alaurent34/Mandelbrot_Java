package utils;

public class Complexe extends Pair<Double, Double>{

	public Complexe(Double a, Double b) {
		super(a, b);
	}

	public Complexe multiplier(double n){
		this.first 	*= n;
		this.second *= n;
		return this;
	}
	
	public Complexe multiplier(Complexe p){
		
		double re = this.first * p.first -	this.second * p.second;
		double im = this.first * p.second + this.second * p.first;
		
		this.first 	= re;
		this.second = im;
		return this;
	}

	public Complexe additionner(Complexe p){
		this.first += p.first;
		this.second += p.second;
		return this;
	}

	public Complexe soustraire(Complexe p){
		this.first -= p.first;
		this.second -= p.second;
		return this;
	}

	public Complexe additionner(double n){
		this.first += n;
		return this;
	}
	
	public Complexe carre(){
		return pow(2);
	}
	
	public Complexe pow(int n){
		Complexe val = new Complexe(this.first, this.second);
		for(int i=1;i<n;i++){
			this.multiplier(val);
		}
		return this;
	}
	
	public double norme(){
		return this.first * this.first + this.second * this.second;
	}
}
