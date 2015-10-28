/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author juandiego
 */
@Entity
@Table(name = "trabajo_investigacion")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TrabajoInvestigacion.findAll", query = "SELECT t FROM TrabajoInvestigacion t"),
    @NamedQuery(name = "TrabajoInvestigacion.findById", query = "SELECT t FROM TrabajoInvestigacion t WHERE t.id = :id"),
    @NamedQuery(name = "TrabajoInvestigacion.findByTitulo", query = "SELECT t FROM TrabajoInvestigacion t WHERE t.titulo = :titulo"),
    @NamedQuery(name = "TrabajoInvestigacion.findByFechaInicio", query = "SELECT t FROM TrabajoInvestigacion t WHERE t.fechaInicio = :fechaInicio"),
    @NamedQuery(name = "TrabajoInvestigacion.findByFechaFin", query = "SELECT t FROM TrabajoInvestigacion t WHERE t.fechaFin = :fechaFin")})
public class TrabajoInvestigacion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "titulo")
    private String titulo;
    @Basic(optional = false)
    @Column(name = "fecha_inicio")
    @Temporal(TemporalType.DATE)
    private Date fechaInicio;
    @Basic(optional = false)
    @Column(name = "fecha_fin")
    @Temporal(TemporalType.DATE)
    private Date fechaFin;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trabajoInvestigacionId")
    private List<Libro> libroList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trabajoInvestigacionId")
    private List<InvestigadorTrabajoInvestigacion> investigadorTrabajoInvestigacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trabajoInvestigacionId")
    private List<Ponencia> ponenciaList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "trabajoInvestigacionId")
    private List<Articulo> articuloList;

    public TrabajoInvestigacion() {
    }

    public TrabajoInvestigacion(Integer id) {
        this.id = id;
    }

    public TrabajoInvestigacion(Integer id, String titulo, Date fechaInicio, Date fechaFin) {
        this.id = id;
        this.titulo = titulo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    @XmlTransient
    public List<Libro> getLibroList() {
        return libroList;
    }

    public void setLibroList(List<Libro> libroList) {
        this.libroList = libroList;
    }

    @XmlTransient
    public List<InvestigadorTrabajoInvestigacion> getInvestigadorTrabajoInvestigacionList() {
        return investigadorTrabajoInvestigacionList;
    }

    public void setInvestigadorTrabajoInvestigacionList(List<InvestigadorTrabajoInvestigacion> investigadorTrabajoInvestigacionList) {
        this.investigadorTrabajoInvestigacionList = investigadorTrabajoInvestigacionList;
    }

    @XmlTransient
    public List<Ponencia> getPonenciaList() {
        return ponenciaList;
    }

    public void setPonenciaList(List<Ponencia> ponenciaList) {
        this.ponenciaList = ponenciaList;
    }

    @XmlTransient
    public List<Articulo> getArticuloList() {
        return articuloList;
    }

    public void setArticuloList(List<Articulo> articuloList) {
        this.articuloList = articuloList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TrabajoInvestigacion)) {
            return false;
        }
        TrabajoInvestigacion other = (TrabajoInvestigacion) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entities.TrabajoInvestigacion[ id=" + id + " ]";
    }
    
}
