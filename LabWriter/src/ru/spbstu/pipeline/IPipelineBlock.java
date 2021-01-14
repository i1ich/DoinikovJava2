package ru.spbstu.pipeline;

public interface IPipelineBlock extends IConfigurable {
    RC setConsumer(IExecutable c);
    RC setProducer(IExecutable p);
}