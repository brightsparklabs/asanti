/*
 * Created by brightSPARK Labs
 * www.brightsparklabs.com
 */

package com.brightsparklabs.asanti.validator;

import com.brightsparklabs.asanti.common.TreeNode;
import com.brightsparklabs.asanti.model.data.DecodedAsnData;
import com.brightsparklabs.asanti.model.schema.AsnSchema;
import com.brightsparklabs.asanti.model.schema.primitive.AsnPrimitiveType;
import com.brightsparklabs.asanti.model.schema.type.*;
import com.brightsparklabs.asanti.validator.builtin.BuiltinTypeValidator;
import com.brightsparklabs.asanti.validator.failure.DecodedTagValidationFailure;
import com.brightsparklabs.asanti.validator.result.DecodedAsnDataValidationResult;
import com.brightsparklabs.asanti.validator.result.ValidationResult;
import com.brightsparklabs.asanti.validator.rule.ValidationRule;
import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.base.Splitter;
import com.google.common.collect.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import static com.google.common.base.Preconditions.*;

/**
 * Default implementation of {@link Validator}.
 *
 * @author brightSPARK Labs
 */
public class ValidatorImpl implements Validator
{
    // -------------------------------------------------------------------------
    // CLASS VARIABLES
    // -------------------------------------------------------------------------

    /** class logger */
    private static final Logger logger = LoggerFactory.getLogger(ValidatorImpl.class);

    /** splitter for separating tag strings */
    private static final Splitter tagSplitter = Splitter.on("/").omitEmptyStrings();

    // -------------------------------------------------------------------------
    // INSTANCE VARIABLES
    // -------------------------------------------------------------------------

    /** visitor to determine which {@link ValidationRule} to apply to a tag */
    private final ValidationVisitor validationVisitor = new ValidationVisitor();

    // -------------------------------------------------------------------------
    // IMPLEMENTATION: Validator
    // -------------------------------------------------------------------------

    @Override
    public ValidationResult validate(DecodedAsnData decodedAsnData)
    {
        final DecodedAsnDataValidationResult.Builder builder
                = DecodedAsnDataValidationResult.builder();
        for (final String tag : decodedAsnData.getTags())
        {
            //            final AsnPrimitiveType type = decodedAsnData.getType(tag).getPrimitiveType();
            //            final BuiltinTypeValidator tagValidator = (BuiltinTypeValidator) type.accept(
            //                    validationVisitor);
            //            final ImmutableSet<DecodedTagValidationFailure> failures = tagValidator.validate(tag,
            //                    decodedAsnData);
            //            builder.addAll(failures);
        }

        // add a failure for each unmapped tag
        for (final String tag : decodedAsnData.getUnmappedTags())
        {
            final DecodedTagValidationFailure failure = new DecodedTagValidationFailure(tag,
                    FailureType.UnknownTag,
                    "Tag could not be decoded against schema");
            builder.add(failure);
        }

        // TODO MJF - tree seems good, but does not align with the DecodedAsnData

        ImmutableSet<String> tags = buildTags(decodedAsnData);
        AsnSchema schema = decodedAsnData.getSchema();

        for (String tag : tags)
        {
            logger.debug(tag);


            final AsnPrimitiveType type = schema.getType(tag).get().getPrimitiveType();
            final BuiltinTypeValidator tagValidator = (BuiltinTypeValidator) type.accept(
                    validationVisitor);
            if (tagValidator != null)
            {
                final ImmutableSet<DecodedTagValidationFailure> failures
                        = tagValidator.validate(tag, decodedAsnData);
                builder.addAll(failures);
            }
        }

        //        // In order to see if there is anything missing, and to check constraints of Set/Sequence OF
        //        // it is probably easiest to build a Tree.
        //        // For each node in the tree we check whether it got all its non-optional components.
        //        // for each SET/SEQUENCE OF we check its constraints.
        //        TreeNode<ZZZ> root = buildTree(decodedAsnData);
        //
        //        TreeTraverser<TreeNode<ZZZ>> traverser = new TreeTraverser<TreeNode<ZZZ>>() {
        //            @Override
        //            public Iterable<TreeNode<ZZZ>> children(final TreeNode<ZZZ> root)
        //            {
        //                return root.getChildren();
        //            }
        //        };
        //
        //        AsnSchemaTypeValidationVisitor asnSchemaTypeValidationVisitor
        //                = new AsnSchemaTypeValidationVisitor();
        //
        //        for(TreeNode<ZZZ> node : traverser.breadthFirstTraversal(root))
        //        {
        //            ZZZ zzz = node.getPayload();
        //            logger.debug("Node {} is {}", zzz.name, zzz.type.getBuiltinType());
        //
        //            try
        //            {
        //                AsnSchemaTypeValidator validator = (AsnSchemaTypeValidator)zzz.type.accept(
        //                        asnSchemaTypeValidationVisitor);
        //                if (validator != null)
        //                {
        //                    final ImmutableSet<DecodedTagValidationFailure> failures = validator.validate(node.getChildren());
        //                    builder.addAll(failures);
        //                }
        //
        //                final AsnPrimitiveType type = zzz.type.getPrimitiveType();
        //                final String tag = getFullyQualifiedTag(node, root);
        //                final BuiltinTypeValidator tagValidator = (BuiltinTypeValidator) type.accept(
        //                        validationVisitor);
        //                if (tagValidator != null)
        //                {
        //                    final ImmutableSet<DecodedTagValidationFailure> failures = tagValidator.validate(tag,
        //                            decodedAsnData);
        //                    builder.addAll(failures);
        //                }
        //            }
        //            catch (ParseException e)
        //            {
        //            }
        //        }

        return builder.build();
    }

    private static class MyPredicate implements Predicate<String>
    {
        private final String tag;

        MyPredicate(String tag)
        {
            this.tag = tag;
        }

        @Override
        public boolean apply(final String input)
        {

            if (!input.startsWith(tag) || input.equals(tag))
            {
                return false;
            }

            // only true for the next level child, not all its children too
            String s = input.substring(tag.length()+1);

            return !s.contains("/");
        }
    }

    private String getFullyQualifiedTag(TreeNode<ZZZ> node, TreeNode<ZZZ> root)
    {
        if (node != root)
        {
            return getFullyQualifiedTag(node.getParent(), root) + "/" + node.getPayload().name;
        }
        return "";
    }

    private ImmutableSet<String> buildTags(DecodedAsnData decodedAsnData)
    {
        Set<String> result = Sets.newHashSet();

        // TODO MJF - we are going to have to work through all tags (even unmapped), because the
        // unmapped tags MAY be the only ones with some of the path in them.
        // Obviously we need to understand where to stop (ie don't get to the unmapped part)
        for (final String tag : decodedAsnData.getTags())
        {
            final ArrayList<String> tags = Lists.newArrayList(tagSplitter.split(tag));

            // we ignore the first one because it is the Module name.
            String reconstructed = "";
            for (int i = 0; i < tags.size(); i++)
            {
                final String tagName = tags.get(i);
                reconstructed += "/" + tagName;
                result.add(reconstructed);
            }
        }
        return ImmutableSortedSet.copyOf(result); // Don't really need it sorted, just makes it easier to work with while debugging
        //return ImmutableSet.copyOf(result);
    }

    private TreeNode<ZZZ> buildTree(DecodedAsnData decodedAsnData)
    {
        TreeNode<ZZZ> root = new TreeNode<ZZZ>(new ZZZ("root", AsnSchemaType.NULL));

        AsnSchema schema = decodedAsnData.getSchema();

        // TODO MJF - we are going to have to work through all tags (even unmapped), because the
        // unmapped tags MAY be the only ones with some of the path in them.
        // Obviously we need to understand where to stop (ie don't get to the unmapped part)
        for (final String tag : decodedAsnData.getTags())
        {
            final ArrayList<String> tags = Lists.newArrayList(tagSplitter.split(tag));

            // we ignore the first one because it is the Module name.
            String reconstructed = "";// "/" + tags.get(0);
            TreeNode<ZZZ> currentNode = root;
            for (int i = 0; i < tags.size(); i++)
            {
                final String tagName = tags.get(i);

                if (tagName.contains("["))
                {

                }

                reconstructed += "/" + tagName;
                Optional<AsnSchemaType> type = schema.getType(reconstructed);
                ZZZ newChild = new ZZZ(tagName, type.get());
                Optional<TreeNode<ZZZ>> exists = currentNode.getChild(newChild);
                if (exists.isPresent())
                {
                    currentNode = exists.get();
                }
                else
                {
                    currentNode = currentNode.addChild(newChild);
                }
            }
        }
        int breakpoint = 0;

        return root;
    }

    private interface AsnSchemaTypeValidator
    {

        ImmutableSet<DecodedTagValidationFailure> validate(Iterable<TreeNode<ZZZ>> elements);
    }

    private static class ConstructedValidator implements AsnSchemaTypeValidator
    {
        AsnSchemaTypeConstructed type;

        ConstructedValidator(AsnSchemaTypeConstructed type)
        {
            this.type = type;
        }

        @Override
        public ImmutableSet<DecodedTagValidationFailure> validate(Iterable<TreeNode<ZZZ>> elements)
        {
            // We want to make sure that all of the non-optional components of this Constructed
            // type are present in the elements, otherwise we have a validation failure.

            ImmutableSet.Builder<String> receivedBuilder = ImmutableSet.builder();
            for (TreeNode<ZZZ> node : elements)
            {
                ZZZ zzz = node.getPayload();
                receivedBuilder.add(zzz.name);
            }

            ImmutableSet<String> receivedComponents = receivedBuilder.build();

            final Set<DecodedTagValidationFailure> failures = Sets.newHashSet();

            final ImmutableList<AsnSchemaComponentType> allComponents = type.getAllComponents();
            for (AsnSchemaComponentType component : allComponents)
            {
                if (!component.isOptional())
                {
                    if (!receivedComponents.contains(component.getName()))
                    {
                        // TODO MJF - get the fully qualified tag that is missing noting that we
                        // may have an empty iterable so may not know how to get to the parents
                        // so we may need a slight interface change
                        // for now just put anything...
                        final DecodedTagValidationFailure failure = new DecodedTagValidationFailure(
                                component.getName(),
                                FailureType.MandatoryFieldMissing,
                                "Mandatory field was not found in the data");
                        failures.add(failure);
                    }
                }
            }

            int breakpoint = 0;

            return ImmutableSet.copyOf(failures);
        }
    }

    private static class CollectionValidator implements AsnSchemaTypeValidator
    {
        AsnSchemaTypeCollection type;

        CollectionValidator(AsnSchemaTypeCollection type)
        {
            this.type = type;
        }

        @Override
        public ImmutableSet<DecodedTagValidationFailure> validate(Iterable<TreeNode<ZZZ>> elements)
        {
            // We should check the Constraints of the Collection

            int breakpoint = 0;

            // Really this can probably be part of the normal Validator mechanism?

            return ImmutableSet.of();
        }
    }

    //private static class AsnSchemaTypeValidationVisitor implements AsnSchemaTypeVisitor<Optional<? extends AsnSchemaTypeValidator>>
    private static class AsnSchemaTypeValidationVisitor
            implements AsnSchemaTypeVisitor<AsnSchemaTypeValidator>
    {
        // ---------------------------------------------------------------------
        // PUBLIC METHODS
        // ---------------------------------------------------------------------

        @Override
        //public Optional<? extends AsnSchemaTypeValidator> visit(AsnSchemaTypeConstructed visitable) throws ParseException
        public AsnSchemaTypeValidator visit(AsnSchemaTypeConstructed visitable)
                throws ParseException
        {
            //return Optional.of(new ConstructedValidator(visitable));
            return new ConstructedValidator(visitable);
        }

        @Override
        public AsnSchemaTypeValidator visit(BaseAsnSchemaType visitable) throws ParseException
        {
            return null;
        }

        @Override
        public AsnSchemaTypeValidator visit(AsnSchemaTypeCollection visitable) throws ParseException
        {
            return new CollectionValidator(visitable);
        }

        @Override
        public AsnSchemaTypeValidator visit(AsnSchemaTypeWithNamedTags visitable)
                throws ParseException
        {
            return null;
        }

        @Override
        public AsnSchemaTypeValidator visit(AsnSchemaTypePlaceholder visitable)
                throws ParseException
        {
            return null;
        }

        @Override
        public AsnSchemaTypeValidator visit(AsnSchemaType.Null visitable) throws ParseException
        {
            return null;
        }
    }

    private static class ZZZ
    {
        String name;

        AsnSchemaType type;

        ZZZ(String name, AsnSchemaType type)
        {
            checkNotNull(name);
            checkNotNull(type);

            this.name = name;
            this.type = type;
        }

        @Override
        public boolean equals(Object o)
        {
            if (!(o instanceof ZZZ))
            {
                return false;
            }
            ZZZ other = (ZZZ) o;
            return (other.name.equals(this.name) && (other.type.equals(type)));
        }

        // TODO MJF - implement hashCode
    }
}
