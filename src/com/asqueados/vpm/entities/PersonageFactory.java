/*
 * PersonageFactory.java
 * 
 * Copyright (c) 2011 Pablo J. Urbano Santos <flamma at member.fsf.org>. 
 * 
 * This file is part of vpm.
 * 
 * vpm is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * vpm is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with vpm.  If not, see <http ://www.gnu.org/licenses/>.
 */

package com.asqueados.vpm.entities;

import com.asqueados.vpm.configuration.Configuration;
import com.asqueados.vpm.exceptions.UnableToCreateArchetypeException;
import com.asqueados.vpm.exceptions.UnableToCreatePersonageException;
import com.asqueados.vpm.view.TraitTypes;
import com.asqueados.vpm.xml.XmlReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class for building characters.
 * 
 * @author Pablo J. Urbano Santos <flamma at member.fsf.org>
 */
public class PersonageFactory {
    private static String defaultTemplateFile;
    private static String templatesPath;
    
    private static void loadProperties(){
        defaultTemplateFile = Configuration.getOption("defaultCharacterTemplate");
        templatesPath = Configuration.getOption("templatesPath");
    }
    
    private static String getDefaultTemplateFile() {
        if ( defaultTemplateFile == null ) loadProperties();
        return defaultTemplateFile;
    }

    private static String getTemplatesPath() {
        if ( templatesPath == null ) loadProperties();
        return templatesPath;
    }

    private static Archetype getRandomArchetype() throws UnableToCreateArchetypeException {
        List<Archetype> archetypes = new ArrayList(ArchetypeFactory.getArchetypes());
        
        Random roller = new Random();
        
        int elected = roller.nextInt(archetypes.size());
        
        return archetypes.get(elected);
    }    
    
    public static Personage createCharacter() throws UnableToCreatePersonageException {
        return createCharacter(null);
    }

    public static Personage createCharacter(String template) throws UnableToCreatePersonageException {
        try {
            String filePath = null;

            if (template == null) {
                filePath = getDefaultTemplateFile();
            } else {
                String dirPath = getTemplatesPath();
                filePath = dirPath + '/' + template + "Character.xml";
            }

            XmlReader reader = new XmlReader(filePath);
            Personage character = reader.readCharacter();

            Archetype archetype = getRandomArchetype();
            
            character.setTrait(new Trait("archetype", "string", archetype.getId()));
            
            List<Trait> traits = archetype.getTraits();

            for (Trait trait : traits) {
                Trait charTrait = character.getTrait(trait.getName());

                if (charTrait == null) {
                    character.setTrait(trait);
                } else {
                    if (trait.isNumeric()) {
                        // Add
                        Integer value = (Integer) trait.getValue();
                        Integer charValue = (Integer) trait.getValue();
                        charValue = value + charValue;
                        charTrait.setValue(charValue);
                    } else {
                        // Replace
                        character.setTrait(trait);
                    }
                }
            }
            
            // Special traits
            // Roll sex
            Trait trait = character.getTrait("sex");
            if(trait == null) {
                Random roller = new Random();
                int rolled = roller.nextInt(2);
                trait = new Trait("sex", Trait.STRING, rolled==0?"female":"male");
                
                character.setTrait(trait);
            }
            
            return character;
        } catch (Exception ex) {
            Logger.getLogger(PersonageFactory.class.getName()).log(Level.SEVERE, null, ex);
            throw new UnableToCreatePersonageException(ex);
        }        
    }

    public static Personage createCharacter(String template, int attPoints
            , int skillPoints) throws UnableToCreatePersonageException {
        
        Personage character = createCharacter(template);
        
        // TODO: Add trait if doesn't exist
        // Increase random attributes
        List<String> incrementables= TraitTypes.getAttributesNames();

        for(int i=0; i<attPoints; i++) {
            Random roller = new Random();      
            int elected = roller.nextInt(incrementables.size());
            Trait trait = character.getTrait(incrementables.get(elected));
            if(trait == null)
                trait = new Trait(incrementables.get(elected), Trait.INTEGER, 1);
            
            trait.inc();
        }

        // Increase random skills
        incrementables = TraitTypes.getSkillsNames();
        for(int i=0; i<skillPoints; i++) {
            Random roller = new Random();        
            int elected = roller.nextInt(incrementables.size());
            Trait trait = character.getTrait(incrementables.get(elected));

            // If he doesn't have the trait, try choosing another
            if(trait == null) {
                elected = roller.nextInt(incrementables.size());
                trait = character.getTrait(incrementables.get(elected));
                
                // Still not found the trait? add it
                if (trait == null) {
                    trait = new Trait(incrementables.get(elected), Trait.INTEGER, 1);
                    character.setTrait(trait);
                }
            }
            
            trait.inc();
        }
        
        return character;
        
    }
    
    public static Personage createCharacter(int attPoints, int skillPoints) 
            throws UnableToCreatePersonageException {
        return createCharacter(null, attPoints, skillPoints);
    }
}
