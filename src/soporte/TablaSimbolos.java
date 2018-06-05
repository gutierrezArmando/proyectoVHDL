package soporte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class TablaSimbolos {

    HashMap<String, Simbolo> tabla;

    public TablaSimbolos(){
        tabla = new HashMap<>();
    }

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

    public void addPalabraReservada(String palabra) {
        tabla.put(palabra, new Simbolo(palabra,"palabra reservada"));
    }

    public void addPinEntrada(String nombrePin, String tipo_dato) {
        tabla.put(nombrePin, new Simbolo(nombrePin, "pin", "entrada", tipo_dato));
    }

    public void addPinSalida(String nombrePin, String tipo_dato) {
        tabla.put(nombrePin, new Simbolo(nombrePin, "pin", "salida", tipo_dato));
    }

    public void addEntidad(String nombreEntidad) {
        tabla.put(nombreEntidad, new Simbolo(nombreEntidad, "entity"));
    }

    public void addArquitectura(String nombreArquitectura) {
        tabla.put(nombreArquitectura, new Simbolo(nombreArquitectura, "architecture"));
    }

    public Simbolo[] getTabla() {
        return (Simbolo[]) tabla.values().toArray();
    }

    public String getStrSimbolos(){
        String cadena="";
        Iterator<String> simbolos = tabla.keySet().iterator();
        while (simbolos.hasNext()) {
            cadena+= tabla.get(simbolos.next()) + "\n";
        }
        return  cadena;
    }

    public ArrayList<Simbolo> getArraSimbolos() {
        ArrayList<Simbolo> arraySimbolos = new ArrayList<>();
        Iterator<String> simbolos = tabla.keySet().iterator();
        while (simbolos.hasNext()) {
            arraySimbolos.add(tabla.get(simbolos.next()));
        }
        return  arraySimbolos;
    }

}
