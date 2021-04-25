# Grassland ecosystem simulation

## Authors:
Jan Marczak & Andras Horvath

A predator-prey simulation involving six wolves, coyotes, rats
frogs, rabbits and grasshoppers in their somewhat natural habitat.
In a simplified manner they age, get hungry, eat, move, breed, 
react to external factors that are currently happening and die from various reasons.
The simulation takes place on a rectangular field which is covered in plants. 
The animals can be either carnivores such as wolves, coyotes, rats, frogs and eat other species,  
or herbivores like rabbits, grasshoppers and be dependent on plants. 


### Running Instructions

    1) Create a Simulator object.
    Then call one of:
        + simulateOneStep - for a single step.
        + simulate - and supply a number (say 10) for that many steps.
        + runLongSimulation - for a simulation of 500 steps.
        
    2) Call static method void main(String[] args) from StartSimulation class
       to run a long simulation.


