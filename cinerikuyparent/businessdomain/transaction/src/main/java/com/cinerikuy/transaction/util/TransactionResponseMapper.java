package com.cinerikuy.transaction.util;

import com.cinerikuy.transaction.dto.TransactionResponse;
import com.cinerikuy.transaction.entity.Transaction;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TransactionResponseMapper {

    @Mappings({
            @Mapping(source = "cinema.cinemaCode", target = "cinemaCode"),
            @Mapping(source = "cinema.cinemaName", target = "cinemaName")})
    TransactionResponse TransactionToTransactionResponse(Transaction source);

    List<TransactionResponse> TransactionListToTransactionResponseList(List<Transaction> source);

    @InheritInverseConfiguration
    Transaction TransactionResponseToTransaction(TransactionResponse srr);

    @InheritInverseConfiguration
    List<Transaction> TransactionResponseToTransactionList(List<TransactionResponse> source);

}
