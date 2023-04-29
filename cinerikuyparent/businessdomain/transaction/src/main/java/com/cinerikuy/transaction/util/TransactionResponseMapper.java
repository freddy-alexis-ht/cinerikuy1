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
            @Mapping(expression = "java(getMovieTotalPrice(source.getMovieData().getMovieTicketPrice(), " +
                                                            "source.getMovieData().getMovieTicketUnits()))",
                    target = "movieTotalPrice")
    })
    TransactionResponse TransactionToTransactionResponse(Transaction source);

    List<TransactionResponse> TransactionListToTransactionResponseList(List<Transaction> source);

    default int getMovieTotalPrice(int ticketPrice, int ticketUnits) {
        return ticketPrice * ticketUnits;
    }
}
