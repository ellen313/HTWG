#Makefile 
CC=gcc
CFLAGS=-Wall -pedantic -Werror -g 
LDFLAGS=-lpthread
RM=rm -f
SOURCES=$(wildcard *.c)
TARGETS=$(SOURCES:.c=)
OBJECTS=$(SOURCES:.c=.o)

.PHONY: all clean

all: $(TARGETS)

%: %.c
	$(CC) $(CFLAGS) $< -o $@ $(LDFLAGS)
clean:
	$(RM) $(TARGETS)