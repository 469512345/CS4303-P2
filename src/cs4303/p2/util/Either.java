package cs4303.p2.util;

import java.util.Optional;

public class Either<A, B> {

	private A a;
	private B b;

	private Either(A a, B b) {
		if (a != null && b != null) {
			throw new IllegalArgumentException("Must have either A or B, not both");
		}
		this.a = a;
		this.b = b;
	}

	public void setA(A a) {
		this.a = a;
		this.b = null;
	}

	public void setB(B b) {
		this.a = null;
		this.b = b;
	}

	public boolean hasA() {
		return a != null;
	}

	public boolean hasB() {
		return b != null;
	}

	public A a() {
		return a;
	}

	public B b() {
		return b;
	}

	public Optional<A> optionalA() {
		return Optional.ofNullable(a);
	}

	public Optional<B> optionalB() {
		return Optional.ofNullable(b);
	}

	public static <A, B> Either<A, B> ofA(A a) {
		return new Either<>(a, null);
	}

	public static <A, B> Either<A, B> ofB(B b) {
		return new Either<>(null, b);
	}

}
