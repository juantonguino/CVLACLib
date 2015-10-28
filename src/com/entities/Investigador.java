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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "investigador")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Investigador.findAll", query = "SELECT i FROM Investigador i"),
    @NamedQuery(name = "Investigador.findByIdentificacion", query = "SELECT i FROM Investigador i WHERE i.identificacion = :identificacion"),
    @NamedQuery(name = "Investigador.findByNombres", query = "SELECT i FROM Investigador i WHERE i.nombres = :nombres"),
    @NamedQuery(name = "Investigador.findByApellidos", query = "SELECT i FROM Investigador i WHERE i.apellidos = :apellidos"),
    @NamedQuery(name = "Investigador.findByActivo", query = "SELECT i FROM Investigador i WHERE i.activo = :activo")})
public class Investigador implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "identificacion")
    private Integer identificacion;
    @Basic(optional = false)
    @Column(name = "nombres")
    private String nombres;
    @Basic(optional = false)
    @Column(name = "apellidos")
    private String apellidos;
    @Basic(optional = false)
    @Column(name = "activo")
    private boolean activo;
    @JoinColumn(name = "gurpos_investigacion_nombre", referencedColumnName = "nombre")
    @ManyToOne(optional = false)
    private GurpoInvestigacion gurposInvestigacionNombre;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "investigadorIdentificacion")
    private List<InvestigadorTrabajoInvestigacion> investigadorTrabajoInvestigacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "investigadorIdentificacion")
    private List<ArticuloAutor> articuloAutorList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "investigadorIdentificacion")
    private List<PonenciaAutor> ponenciaAutorList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "investigadorIdentificacion")
    private List<NivelFormacion> nivelFormacionList;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "investigadorIdentificacion")
    private List<LibroAutores> libroAutoresList;
    @OneToMany(mappedBy = "investigadorIdentificacion")
    private List<Usuario> usuarioList;

    public Investigador() {
    }

    public Investigador(Integer identificacion) {
        this.identificacion = identificacion;
    }

    public Investigador(Integer identificacion, String nombres, String apellidos, boolean activo) {
        this.identificacion = identificacion;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.activo = activo;
    }

    public Integer getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(Integer identificacion) {
        this.identificacion = identificacion;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public boolean getActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public GurpoInvestigacion getGurposInvestigacionNombre() {
        return gurposInvestigacionNombre;
    }

    public void setGurposInvestigacionNombre(GurpoInvestigacion gurposInvestigacionNombre) {
        this.gurposInvestigacionNombre = gurposInvestigacionNombre;
    }

    @XmlTransient
    public List<InvestigadorTrabajoInvestigacion> getInvestigadorTrabajoInvestigacionList() {
        return investigadorTrabajoInvestigacionList;
    }

    public void setInvestigadorTrabajoInvestigacionList(List<InvestigadorTrabajoInvestigacion> investigadorTrabajoInvestigacionList) {
        this.investigadorTrabajoInvestigacionList = investigadorTrabajoInvestigacionList;
    }

    @XmlTransient
    public List<ArticuloAutor> getArticuloAutorList() {
        return articuloAutorList;
    }

    public void setArticuloAutorList(List<ArticuloAutor> articuloAutorList) {
        this.articuloAutorList = articuloAutorList;
    }

    @XmlTransient
    public List<PonenciaAutor> getPonenciaAutorList() {
        return ponenciaAutorList;
    }

    public void setPonenciaAutorList(List<PonenciaAutor> ponenciaAutorList) {
        this.ponenciaAutorList = ponenciaAutorList;
    }

    @XmlTransient
    public List<NivelFormacion> getNivelFormacionList() {
        return nivelFormacionList;
    }

    public void setNivelFormacionList(List<NivelFormacion> nivelFormacionList) {
        this.nivelFormacionList = nivelFormacionList;
    }

    @XmlTransient
    public List<LibroAutores> getLibroAutoresList() {
        return libroAutoresList;
    }

    public void setLibroAutoresList(List<LibroAutores> libroAutoresList) {
        this.libroAutoresList = libroAutoresList;
    }

    @XmlTransient
    public List<Usuario> getUsuarioList() {
        return usuarioList;
    }

    public void setUsuarioList(List<Usuario> usuarioList) {
        this.usuarioList = usuarioList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (identificacion != null ? identificacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Investigador)) {
            return false;
        }
        Investigador other = (Investigador) object;
        if ((this.identificacion == null && other.identificacion != null) || (this.identificacion != null && !this.identificacion.equals(other.identificacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.entities.Investigador[ identificacion=" + identificacion + " ]";
    }
    
}
