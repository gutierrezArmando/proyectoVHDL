package soporte;

import java.util.HashMap;

public class TablaSimbolos {

    HashMap<String, Simbolo> tabla;

    public TablaSimbolos(){
        tabla = new HashMap<>();
    }

    public HashMap<String, Simbolo> getTabla() {
        return tabla;
    }

    public void setTabla(HashMap<String, Simbolo> tabla) {
        this.tabla = tabla;
    }

    public void put(String nombre, Simbolo simbolo) {
        tabla.put(nombre, simbolo);
    }

    public Simbolo get(String nombre) {
        return tabla.get(nombre);
    }

    public boolean exist(String nombre) {
        return tabla.get(nombre)!=null?true:false;
    }

}
