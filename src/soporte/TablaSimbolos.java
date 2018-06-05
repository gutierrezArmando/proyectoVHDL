package soporte;

import java.util.ArrayList;
import java.util.HashMap;

public class TablaSimbolos {

    HashMap<String, Simbolo> tabla;

    public TablaSimbolos(){
        tabla = new HashMap<>();
    }

//    public HashMap<String, Simbolo> getTabla() {
//        return tabla;
//    }

//    public void setTabla(HashMap<String, Simbolo> tabla) {
//        this.tabla = tabla;
//    }

    public void put(String clave, Simbolo simbolo) {
        tabla.put(clave, simbolo);
    }

    public Simbolo get(String clave) {
        return tabla.get(clave);
    }

    public boolean exist(String clave) { return tabla.containsKey(clave); }

    public String getClase(String clave) { return tabla.get(clave).getClase(); }

    public String getTipoES(String clave) { return tabla.get(clave).getTipo_es(); }

    public String getTipoDato(String clave) { return tabla.get(clave).getTipo_dato(); }

    public Simbolo[] getTabla() {
        return (Simbolo[]) tabla.values().toArray();
    }

}
