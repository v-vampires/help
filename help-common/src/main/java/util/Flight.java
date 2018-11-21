package util;

import com.google.common.base.Objects;
import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Created by fitz.li on 2018/10/26.
 */
public class Flight {
    private String flightNo;
    private String dep;
    private String arr;

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    public void setDep(String dep) {
        this.dep = dep;
    }

    public void setArr(String arr) {
        this.arr = arr;
    }

    public Flight(String flightNo, String dep, String arr) {
        this.flightNo = flightNo;
        this.dep = dep;
        this.arr = arr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Flight flight = (Flight) o;
        return Objects.equal(flightNo, flight.flightNo) &&
                Objects.equal(dep, flight.dep) &&
                Objects.equal(arr, flight.arr);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(flightNo, dep, arr);
    }

    @Override
    public String toString() {
        return "Flight{" +
                "flightNo='" + flightNo + '\'' +
                ", dep='" + dep + '\'' +
                ", arr='" + arr + '\'' +
                '}';
    }

    public static void main(String[] args) {
        Flight f = new Flight("A101","PEK","BKK");
        Set<Flight> set = Sets.newHashSet();
        set.add(f);
        f.setFlightNo("A102");
        set.add(f);
        System.out.println(set);

    }
}
