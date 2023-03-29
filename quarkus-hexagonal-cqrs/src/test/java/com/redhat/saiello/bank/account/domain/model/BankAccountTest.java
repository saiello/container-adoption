package com.redhat.saiello.bank.account.domain.model;

import com.redhat.saiello.bank.account.domain.model.events.AmountDeposited;
import com.redhat.saiello.bank.account.domain.model.events.AmountWithdrawn;
import com.redhat.saiello.bank.account.domain.model.events.BankAccountCreated;
import com.redhat.saiello.common.OutboxItem;
import io.debezium.outbox.quarkus.ExportedEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

class BankAccountTest {


    @Test void create_a_bank_account_passing_a_personal_info(){
        // given
        var personalInfo = new PersonalInfo("John", "Doe");

        // when
        var resultWithEvents = BankAccount.create(personalInfo);
        var account = resultWithEvents.result();
        var events = resultWithEvents.events();

        // then
        assertThat(account.balance, equalTo(0d));
        assertThat(events, contains(instanceOf(BankAccountCreated.class)));
    }


    @Nested
    class WhenNew{

        BankAccount account;

        @BeforeEach
        void prepare(){
            // given
            var resultWithEvents = BankAccount.create(new PersonalInfo("John", "Doe"));
            account = resultWithEvents.result();
        }

        @Test void withdraw_should_throw_an_exception(){
            Assertions.assertThrows(Exception.class, () -> account.withdraw(10d));
        }

        @Test void deposit_should_succeed(){
            var events = account.deposit(10d);
            assertThat(account.balance, equalTo(10d));
            assertThat(events, contains(
                    instanceOf(AmountDeposited.class)
            ));
        }

        @Nested
        class WhenBalanceIsPositive{

            @BeforeEach
            void prepare(){
                account.deposit(10d); // given
            }

            @Test void withdraw_exceeding_balance_should_throw_an_exception(){
                Assertions.assertThrows(Exception.class, () -> account.withdraw(11d));
            }

            @Test void withdraw_should_succeed(){
                var events = account.withdraw(10d);
                assertThat(account.balance, equalTo(0d));
                assertThat(events, contains(
                        instanceOf(AmountWithdrawn.class)
                ));
            }

            @Test void deposit_should_succeed(){
                account.deposit(200d);
                assertThat(account.balance, equalTo(210d));
            }
        }

    }



}