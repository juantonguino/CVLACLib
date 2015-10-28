/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entities;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author juandiego
 */
@Entity
@Table(name = "gurpo_investigacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "GurpoInvestigacion.findAll", query = "SELECT g FROM GurpoInvestigacion g"),
    @NamedQuery(name = "GurpoInvestigacion.findByNombre", query = "SELECT g FROM GurpoInvestigacion g WHERE g.nombre = :nombre"),
    @NamedQuery(name = "GurpoInvestigacion.findByLineaInvestigacion", query = "SELECT g FROM GurpoInvestigacion g WHERE g.lineaInvestigacion = :lineaInvestigacion"),
    @NamedQuery(name = "GurpoInvestigacion.findByCategorizacionGrupo", query = "SELECT g FROM GurpoInvestigacion g WHERE g.categorizacionGrupo = :categorizacionGrupo")})
public class GurpoInvestigacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "nombre")
    private String nombre;
    @Basic(optional = false)
    @Column(name = "linea_investigacion")
    private String lineaInvestigacion;
    @Basic(optional = false)
    @Column(name = "categorizacion_grupo")
    private String categorizacionGrupo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gurposInvestigacionNombre")
    private List<Investigador> investigadorList;

    public GurpoInvestigacion() {
    }

    public GurpoInvestigacion(String nombre) {
        this.nombre = nombre;
    }

    public GurpoInvestigacion(String nombre, String lineaInvestigacion, String categorizacionGrupo) {
        this.nombre = nombre;
        this.lineaInvestigacion = lineaInvestigacion;
        this.categorizacionGrupo = categorizacionGrupo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getLineaInvestigacion() {
        return lineaInvestigacion;
    }

    public void setLineaInvestigacion(String lineaInvestigacion) {
        this.lineaInvestigacion = lineaInvestigacion;
    }

    public String getCategorizacionGrupo() {
        return categorizacionGrupo;
    }

    public void setCategorizacionGrupo(String categorizacionGrupo) {
        this.categorizacionGrupo = categorizacionGrupo;
    }

    @XmlTransient
    public List<Investigador> getInvestigadorList() {
        return investigadorList;
    }

    public void setInvestigadorList(List<Investigador> investigadorList) {
        this.investigadorList = investigadorList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (nombre != null ? nombre.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof GurpoInvestigacion)) {
            return false;
        }
        GurpoInvestigacion other = (GurpoInvestigacion) object;
        if ((this.nombre == null && other.nombre != null) || (this.nombre != null && !this.nombre.equals(other.nombre))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entities.GurpoInvestigacion[ nombre=" + nombre + " ]";
    }
    
}
