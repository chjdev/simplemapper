Little library to explore java annotations and reflection. This is experimental code and not intended to be used in production.

Allows to map fields of an arbitrary inputobject that is traversable by a list of strings to the fields of an annotated POJO.

The fields of the POJO have to be either public, or provide a public setter method.

The value of the input object can be transformed by a chain of ITRansformers. 
The mapping will fail on NULL values. This behaviour can be changed by setting "nullable" to true.

The type is inferred from the mapped field in the POJO. The mapping will fail if the type does not match exactly (note: in the provided simpleaccessor implementation the corresponding wrapper classes for primitive data types have to be used). This behaviour can be changed by setting the "lenient" parameter to true. In this case the accessor implementation, or an appropriate ITransformer chain has to take care of the type mapping.

"lenient" and "nullable" can be set on top level to set it for all fields of the class.

See the provided example on how to use the library.
