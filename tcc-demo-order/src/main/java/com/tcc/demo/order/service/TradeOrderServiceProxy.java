package com.tcc.demo.order.service;

import org.mengyun.tcctransaction.api.Compensable;
import org.mengyun.tcctransaction.api.Propagation;
import org.mengyun.tcctransaction.api.TransactionContext;
import org.mengyun.tcctransaction.context.MethodTransactionContextEditor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tcc.demo.order.dto.CapitalTradeOrderDto;
import com.tcc.demo.order.dto.RedPacketTradeOrderDto;
import com.tcc.demo.order.feign.CapitalTradeOrderService;
import com.tcc.demo.order.feign.RedPacketTradeOrderService;

import io.prong.boot.tcc.support.TransactionEntity;

/**
 * Created by changming.xie on 4/19/17.
 */
@Component
public class TradeOrderServiceProxy {

    @Autowired
    CapitalTradeOrderService capitalTradeOrderService;

    @Autowired
    RedPacketTradeOrderService redPacketTradeOrderService;

    /*the propagation need set Propagation.SUPPORTS,otherwise the recover doesn't work,
      The default value is Propagation.REQUIRED, which means will begin new transaction when recover.
    */
    @Compensable(propagation = Propagation.SUPPORTS, confirmMethod = "record", cancelMethod = "record", transactionContextEditor = MethodTransactionContextEditor.class)
    public String record(TransactionContext transactionContext, CapitalTradeOrderDto tradeOrderDto) {
        return capitalTradeOrderService.record(new TransactionEntity<>(transactionContext, tradeOrderDto));
    }

    @Compensable(propagation = Propagation.SUPPORTS, confirmMethod = "record", cancelMethod = "record", transactionContextEditor = MethodTransactionContextEditor.class)
    public String record(TransactionContext transactionContext, RedPacketTradeOrderDto tradeOrderDto) {
        return redPacketTradeOrderService.record(new TransactionEntity<>(transactionContext, tradeOrderDto));
    }
}
