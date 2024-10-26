#Makefile bin

#Kommandovariablen
CXX = g++
CXXFLAGS = -g -W -Wall -Weffc++ -Wold-style-cast -std=$(STD) -fpic
CPPFLAGS = -I$(LIBDIR)
#erster Parameter sagt dem statischen linker wo die Bib ist, zweiter sagt dem dynamischen linker wo die bib ist
LDFLAGS = -L$(LIBDIR) -Wl,-rpath=$(LIBDIR)
STD = c++11
RM = rm -f

#Hilfsvariablen
TARGET = notenspiegel
OBJECTS = \
	$(TARGET).o
SOURCES = $(OBJECTS:.o=.cpp)
HEADERS = \
	$(LIBDIR)/benotung.h \
	$(LIBDIR)/fachnote.h \
	$(LIBDIR)/fachnoten_liste.h
LIBDIR = ../lib
LIBNAME = aufgabe6
LIBTYPE = a

#Musterregeln
%.o: %.cpp
	$(CXX) $(CPPFLAGS) $(CXXFLAGS) -c $< -o $@

#Standardziele
.PHONY: all clean

all: $(TARGET)

clean:
	$(RM) $(TARGET) $(OBJECTS) depend

depend: $(SOURCES) $(HEADERS)
	$(CXX) $(CPPFLAGS) -MM $(SOURCES) > $@

#Ziele zur Programmerstellung
$(TARGET): $(OBJECTS) $(LIBDIR)/lib$(LIBNAME).$(LIBTYPE)
	$(CXX) $(CPPFLAGS) $(LDFLAGS) -o $@ $< -l:lib$(LIBNAME).$(LIBTYPE)

#Abhaenigkeiten
include depend