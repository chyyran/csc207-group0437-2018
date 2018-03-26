package edu.toronto.csc207.restaurantsolution.model.interfaces;

import java.io.Serializable;

/**
 * Represents an Ingredient in the stock.
 */
public interface Ingredient extends Serializable {
    /**
     * Returns the name of the Ingredient.
     *
     * @return the name of the Ingredient.
     */
    String getName();

    /**
     * Returns the cost of the Ingredient.
     *
     * @return the cost of the Ingredient.
     */
    Double getCost();

    /**
     * Returns the pricing of a single unit of Ingredient.
     *
     * @return the pricing of a single unit of Ingredient.
     */
    Double getPricing();

    /**
     * Returns the threshold for reordering.
     * If the amount of Ingredient in stock is less than the threshold, we request for reorder.
     *
     * @return the threshold for reordering.
     */
    Integer getReorderThreshold();

    /**
     * Returns the default amount of Ingredient for reordering.
     *
     * @return the default amount of Ingredient for reordering.
     */
    Integer getDefaultReorderAmount();

    /**
     * Checks whether ingredient is the same as this.
     *
     * @param ingredient the Ingredient to be compared with this
     * @return true if ingredient and this are the same; false otherwise.
     */
    default boolean equals(Ingredient ingredient) {
        return this.getName().equals(ingredient.getName());
    }
}
