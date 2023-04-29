package com.cinerikuy.transaction.entity;

public enum StatusEnum {
    PENDIENTE_DE_PAGO {
        @Override
        public String toString() { return "Pendiente de pago"; }
    }, PAGO_COMPLETO {
        @Override
        public String toString() { return "Pagado con éxito"; }
    }, PAGO_INCOMPLETO {
        @Override
        public String toString() { return "Pago incompleto realizado"; }
    };
}
