package ru.hse.se.types;

import java.util.ArrayList;

@SuppressWarnings("unchecked")
public abstract class MFType<T> extends ValueType {
    
    public MFType(ArrayList<T> value) {
        this.value = (ArrayList<T>)(value.clone());
    }
    
    public MFType() {
        this.value = new ArrayList<T>();
    }
    
    public ArrayList<T> getValue() {
        return (ArrayList<T>)(this.value.clone());
    }
    
    public void add(T t) {
        this.value.add(t);
    }
    
    public void remove(T t) {
        this.value.remove(t);
    }
    
    public void remove(int i) {
        this.value.remove(i);
    }
    
    public int size() {
        return this.value.size();
    }
    
    @Override
    public String toString() {
        
        StringBuilder res = new StringBuilder();
        
        res.append("[ ");
        for (T val : value) {
            res.append(val.toString());
            res.append(' ');
        }
        res.append("]");
        
        return res.toString();
    }
    
    protected ArrayList<T> value;
}
