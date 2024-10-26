#Makefile lib

#Kommandovariablen
CXX = g++
CXXFLAGS = -g -W -Wall -Wextra -Weffc++ -Wold-style-cast -std=$(STD) -fpic
CPPFLAGS = -I.
STD = c++11
RM = rm -f
SL = ar rvs
DL = -shared

#Hilfsvariablen
TARGET = lib$(LIBNAME)
LIBNAME = aufgabe6
LIBTYPE = a
OBJECTS = \
	benotung.o \
	fachnote.o \
	fachnoten_liste.o
SOURCES = $(OBJECTS:.o=.cpp)
HEADERS = $(OBJECTS:.o=.h)

#Musterregeln
%.o: %.cpp
	$(CXX) $(CPPFLAGS) $(CXXFLAGS) -c $< -o $@

#Standardziele
.PHONY: all clean

all: $(TARGET).$(LIBTYPE)

clean:
	$(RM) $(TARGET).a $(TARGET).so $(OBJECTS) depend

depend: $(SOURCES) $(HEADERS)
	$(CXX) $(CPPFLAGS) -MM $(SOURCES) > $@

#Ziele zur Programmerstellung
$(TARGET).a: $(OBJECTS)
	$(SL) $@ $^

$(TARGET).so: $(OBJECTS)
	$(CXX) $(DL) -o $@ $^

#Abhaenigkeiten
include depend
