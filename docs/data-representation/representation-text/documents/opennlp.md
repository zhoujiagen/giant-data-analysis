实践版本: 1.7.1.

> The Apache OpenNLP library is a machine learning based toolkit for the processing of natural language text. It supports the most common NLP tasks, such as tokenization, sentence segmentation, part-of-speech tagging, named entity extraction, chunking, parsing, and coreference resolution. These tasks are usually required to build more advanced text processing services. OpenNLP also included maximum entropy and perceptron based machine learning.

> The goal of the OpenNLP project will be to create a mature toolkit for the abovementioned tasks. An additional goal is to provide a large number of pre-built models for a variety of languages, as well as the annotated text resources that those models are derived from.

# 1 资源

[OpenNLP文档目录](https://opennlp.apache.org/documentation.html)
[OpenNLP1.7.1手册](https://opennlp.apache.org/documentation/1.7.1/manual/opennlp.html)

# 2 特性

+ Sentence Detector

	TODO 训练数据文件格式
	
+ Tokenizer
+ Name Finder
+ Document Categorizer

	TODO 训练数据文件格式

+ Part-of-Speech Tagger

+ Lemmatizer

	TODO 训练数据文件格式
	
+ Chunker
+ Parser
+ Coreference Resolution

+ Corpora
+ Machine Learning - Maximum Entropy

+ Command Line Interface

	### Basic command structure
	# tool: TokenizerTrainer
	# format parameter: [.namefinder|.conllx|.pos]
	# optional parameter: [-abbDict path]
	# obligatory parameter: -model modelFile
	$ bin/opennlp TokenizerTrainer[.namefinder|.conllx|.pos] [-abbDict path] ...  -model modelFile ...

	### Play with Tool, Trainer, Evaluator
	# tool
	$ bin/opennlp ToolName lang-model-name.bin < input.txt > output.txt
	# trainer
	$ bin/opennlp ToolNameTrainer -model en-model-name.bin -lang en -data input.train -encoding UTF-8
	# evaluator
	$ bin/opennlp ToolNameEvaluator -model en-model-name.bin -lang en -data input.test -encoding UTF-8

# 3 问题

+ 如何生成模型(模型文件)?


