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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "articulo")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Articulo.findAll", query = "SELECT a FROM Articulo a"),
    @NamedQuery(name = "Articulo.findByIdArticulo", query = "SELECT a FROM Articulo a WHERE a.idArticulo = :idArticulo"),
    @NamedQuery(name = "Articulo.findByAnioPublicacion", query = "SELECT a FROM Articulo a WHERE a.anioPublicacion = :anioPublicacion"),
    @NamedQuery(name = "Articulo.findByNombreArticulo", query = "SELECT a FROM Articulo a WHERE a.nombreArticulo = :nombreArticulo")})
public class Articulo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_articulo")
    private Integer idArticulo;
    @Basic(optional = false)
    @Column(name = "anio_publicacion")
    @Temporal(TemporalType.DATE)
    private Date anioPublicacion;
    @Basic(optional = false)
    @Column(name = "nombre_articulo")
    private String nombreArticulo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "articuloIdArticulo")
    private List<ArticuloAutor> articuloAutorList;
    @JoinColumn(name = "revista_nombre", referencedColumnName = "nombre")
    @ManyToOne(optional = false)
    private Revista revistaNombre;
    @JoinColumn(name = "trabajo_investigacion_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private TrabajoInvestigacion trabajoInvestigacionId;

    public Articulo() {
    }

    public Articulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public Articulo(Integer idArticulo, Date anioPublicacion, String nombreArticulo) {
        this.idArticulo = idArticulo;
        this.anioPublicacion = anioPublicacion;
        this.nombreArticulo = nombreArticulo;
    }

    public Integer getIdArticulo() {
        return idArticulo;
    }

    public void setIdArticulo(Integer idArticulo) {
        this.idArticulo = idArticulo;
    }

    public Date getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(Date anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public String getNombreArticulo() {
        return nombreArticulo;
    }

    public void setNombreArticulo(String nombreArticulo) {
        this.nombreArticulo = nombreArticulo;
    }

    @XmlTransient
    public List<ArticuloAutor> getArticuloAutorList() {
        return articuloAutorList;
    }

    public void setArticuloAutorList(List<ArticuloAutor> articuloAutorList) {
        this.articuloAutorList = articuloAutorList;
    }

    public Revista getRevistaNombre() {
        return revistaNombre;
    }

    public void setRevistaNombre(Revista revistaNombre) {
        this.revistaNombre = revistaNombre;
    }

    public TrabajoInvestigacion getTrabajoInvestigacionId() {
        return trabajoInvestigacionId;
    }

    public void setTrabajoInvestigacionId(TrabajoInvestigacion trabajoInvestigacionId) {
        this.trabajoInvestigacionId = trabajoInvestigacionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idArticulo != null ? idArticulo.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Articulo)) {
            return false;
        }
        Articulo other = (Articulo) object;
        if ((this.idArticulo == null && other.idArticulo != null) || (this.idArticulo != null && !this.idArticulo.equals(other.idArticulo))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entities.Articulo[ idArticulo=" + idArticulo + " ]";
    }
    
}
