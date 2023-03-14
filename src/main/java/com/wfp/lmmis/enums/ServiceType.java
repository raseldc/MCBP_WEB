/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wfp.lmmis.enums;

/**
 *
 * @author Philip
 */
public enum ServiceType {

    /**
     *
     */
    DOUBLEDIPPING,
    NIDVERIFICATION,
    RECONCILIATION,
    SUPPLEMENTARY,
    PAYROLL,
    PAYMENTCYCLE,

    /**
     *
     */
    MUKTOPATH,
    NIDIBAS;

    @Override
    public String toString() {
        return name();
    }
}
