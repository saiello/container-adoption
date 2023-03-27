package com.redhat.saiello.bank.account.domain.model;

import com.redhat.saiello.bank.account.domain.model.events.BankAccountCreated;
import com.redhat.saiello.es.Event;
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
        var account = BankAccount.create(personalInfo);

        // then
        assertThat(account.balance, equalTo(0d));
        assertThat(account.changes(), contains(
                        allOf(
                                instanceOf(Event.class),
                                hasProperty("aggregateType", equalTo("BankAccount")),
                                hasProperty("aggregateId", equalTo(account.id())),
                                hasProperty("type", equalTo(BankAccountCreated.TYPE)),
                                hasProperty("payload", equalTo("{\"personalInfo\":{\"firstName\":\"John\",\"lastName\":\"Doe\"}}"))
                        )
                )
        );
    }


    @Nested
    class WhenNew{

        BankAccount account;

        @BeforeEach
        void prepare(){
            // given
            account = BankAccount.create(new PersonalInfo("John", "Doe"));
        }

        @Test void withdraw_should_throw_an_exception(){
            Assertions.assertThrows(Exception.class, () -> account.withdraw(10d));
        }

        @Test void deposit_should_succeed(){
            account.deposit(10d);
            assertThat(account.balance, equalTo(10d));
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
                account.withdraw(10d);
                assertThat(account.balance, equalTo(0d));
            }

            @Test void deposit_should_succeed(){
                account.deposit(200d);
                assertThat(account.balance, equalTo(210d));
            }

            @Test void testEvents(){
                List<Event> changes = account.changes();
                assertThat(changes, contains(
                        allOf(
                                instanceOf(Event.class),
                                hasProperty("payload", equalTo("{\"personalInfo\":{\"firstName\":\"John\",\"lastName\":\"Doe\"}}"))
                        ),
                        allOf(
                                instanceOf(Event.class),
                                hasProperty("payload", equalTo("{\"amount\":10.0}"))
                        )
                ));
            }

            @Nested
            class WhenEventsOccurred{

                List<Event> events;

                @BeforeEach void prepare(){
                    events = account.changes();
                }

                @Test void testEvents(){
                    BankAccount bankAccount = new BankAccount("id");
                    bankAccount.load(events);

                    assertThat(bankAccount.changes(), is(empty()));
                    assertThat(bankAccount.version(), equalTo(2L));
                    assertThat(bankAccount.balance, equalTo(10d));
                    assertThat(bankAccount.profile, equalTo(new PersonalInfo("John", "Doe")));
                }
            }
        }

    }



}